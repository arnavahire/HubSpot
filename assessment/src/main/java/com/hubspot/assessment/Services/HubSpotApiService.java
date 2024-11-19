package com.hubspot.assessment.Services;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.assessment.Configurations.CustomAppProperties;
import com.hubspot.assessment.Constants.ApiEndpoints;
import com.hubspot.assessment.Models.CallRecord;
import com.hubspot.assessment.Models.CallRecords;
import com.hubspot.assessment.Models.Event;
import com.hubspot.assessment.Models.Result;
import com.hubspot.assessment.Models.Results;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * HubSpotApiService - Holds our business Logic
 */
@Service
public class HubSpotApiService {
    // using okhttpclient and objectmapper via dependency injection
    private OkHttpClient okHttpClient;
    private ObjectMapper objectMapper;
    // also using customer app properties using dependency injection so that we don't need instantiate the object
    @Autowired
    private CustomAppProperties customAppProperties;

    /* Constructor */
    public HubSpotApiService(OkHttpClient okHttpClient, ObjectMapper objectMapper, CustomAppProperties customAppProperties){
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.customAppProperties = customAppProperties;
    }

    /*
     * getCallRecords - Used for fetching call records from the GET endpoint of HubSpot API
     */
    public CallRecords getCallRecords(){
        // Create url from constants and the user key 
        String url = ApiEndpoints.BASE_URL + ApiEndpoints.GET_ENDPOINT + customAppProperties.getUserKey();

        // OkHttp Request object
        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            // Execute request and return an OkHttp Response object
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code: " + response.code());
            }
            String resp = response.body().string();
            // map the response to CallRecords using Jackson databinds's ObjectMapper
            CallRecords callRecords = objectMapper.readValue(resp, new TypeReference<CallRecords>() {});
            return callRecords;
        }
        catch(Exception e){
            throw new RuntimeException("Failed to fetch data", e);
        }
    }

    /*
     * process - Here we will process all the call records and create results
     */
    public Results process(CallRecords callRecords){
        /*  Usually we think of mapping a list of records to customer id. 
            But here, since we want to find multiple concurrent day for a single day, it's better if we map
            list of records to a customerId + date combo so that for every customer-date we can have have separate logs. 
            That would make finding concurrent calls much easier.
        */
        Map<String, List<CallRecord>> customerAndDateToCallRecordsMap = new HashMap<>(); 

        // all calls starting on same day will be stored in the hashmap as values for (customerId + date) combo key
        for(CallRecord callRecord : callRecords.getRecords()){

            int customerId = callRecord.getCustomerId();
            long startTimestamp = callRecord.getStartTimestamp();

            LocalDate startDate = Instant.ofEpochMilli(startTimestamp).atZone(ZoneId.of("UTC")).toLocalDate();
            LocalDate endDate = Instant.ofEpochMilli(callRecord.getEndTimestamp()).atZone(ZoneId.of("UTC")).toLocalDate();

            
            // Single call can last for multiple days. So for such a call, we will need entries in the map for every day including the last day.
            
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // combined customerId and Date key = id+":"+date
                String key = customerId + ":" + currentDate;
                customerAndDateToCallRecordsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(callRecord);
                currentDate = currentDate.plusDays(1);
            }
        }

        List<Result> resultsList = new ArrayList<>();

        for(String key : customerAndDateToCallRecordsMap.keySet()){
            // NOTE: here all the processing will happen for 1 customer for 1 date since our key is customerId:date
            
            String[] splits = key.split(":");
            
            // get the customer id and start date 
            int customerId = Integer.valueOf(splits[0]);
            String startDate = splits[1];

            // callsInfo will have all call records for a particular date for a customer
            List<CallRecord> callsInfo = customerAndDateToCallRecordsMap.get(key);

            List<Event> events = new ArrayList<>();

            for(CallRecord callInfo : callsInfo){
                // for every call's start time and end time, create a separate event and add it to the event list
                long startTimestamp = callInfo.getStartTimestamp();
                long endTimestamp = callInfo.getEndTimestamp();
                
                Event eventwithStartTime = new Event(startTimestamp, true, callInfo);
                Event eventwithEndTime = new Event(endTimestamp, false, callInfo);

                events.add(eventwithStartTime);
                events.add(eventwithEndTime);
            }

            // sort events in ascending order of startTimestamp. If 2 events have same timestamp, start events (marked by isStart bool var) will be chosen compared to end events.
            events.sort((one, two) -> {
                int timestampComparison = Long.compare(one.getTimestamp(), two.getTimestamp());
                if (timestampComparison != 0) return timestampComparison;
                return Boolean.compare(one.getIsStart(), two.getIsStart());
            });

            int maxConcurrentCalls = 0; // stores maxConcurrentCalls
            int concurrentCalls = 0; // stores concurrentCalls reached at any time during processing
            long maxTimestamp = 0; // maxTimestamp

            List<String> maxCallRecordIds = new ArrayList<>(); // stores the callRecordIds corresponding to maximumConcurrentCalls
            List<CallRecord> activeCalls = new ArrayList<>(); // stores the calls that are ongoing

            for(Event event : events){
                // if event is start event, update concurrentCalls count and add the call record to active calls
                if(event.getIsStart()){
                    concurrentCalls++;
                    activeCalls.add(event.getCallRecord());
                    // if concurrentCalls exceed maxConcurrentCalls then update maxConcurrentCalls, maxCallRecordIds and maxTimestamp
                    if(concurrentCalls > maxConcurrentCalls){
                        maxConcurrentCalls = concurrentCalls;
                        maxCallRecordIds = activeCalls.stream().map(CallRecord::getCallId).collect(Collectors.toList()); // go through all active calls and get all callRecordsIds
                        maxTimestamp = event.getTimestamp();
                    }
                }
                // if it is end event, decrement concurrentCalls and remove the call record from active calls
                else{
                    concurrentCalls--;
                    activeCalls.remove(event.getCallRecord());
                }
            }

            // store the result in our Result object
            Result result = new Result(customerId, startDate, maxConcurrentCalls, maxTimestamp, maxCallRecordIds);
                
            resultsList.add(result);
        }
        // store the final output in Results model and return
        Results results = new Results(resultsList);
        return results;
    }

    /* postResults - The final output is sent to the HubSpot POST endpoint */
    public void postResults(Results results){
        try{
            // Store results in json as string
            String jsonPayload = objectMapper.writeValueAsString(results);
            System.out.println(jsonPayload);

            // create okHttp request body
            RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json"));

            String url = ApiEndpoints.BASE_URL + ApiEndpoints.POST_ENDPOINT + customAppProperties.getUserKey();
            // create the okHttp object
            Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            // execute the request and get the response
            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("Failed to submit results: " + response);
            System.out.println("Results submitted successfully!");
        }
        catch(JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }
        catch(IOException exception){
            System.out.println(exception.getMessage());
        }
    }

}
