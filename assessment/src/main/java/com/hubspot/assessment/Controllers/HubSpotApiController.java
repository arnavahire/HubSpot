package com.hubspot.assessment.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.hubspot.assessment.Models.CallRecords;
import com.hubspot.assessment.Models.Results;
import com.hubspot.assessment.Services.HubSpotApiService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

/*  
HubSpotApiController - Controller that maps to "/hubspot-api" uri. 
I created our custom api uri "/hubspot-api" which when hit, will internally make calls to the GET and POST endpoints of the HubSpot API
*/
@RestController
@RequestMapping("/hubspot-api")
public class HubSpotApiController{

    private final HubSpotApiService hubspotApiService;

    public HubSpotApiController(HubSpotApiService hubspotApiService){
        this.hubspotApiService = hubspotApiService;
    }

    // Calling Get on "http://localhost:8080/hubspot-api/fetch-data" will redirect to our business logic for handling input from HubSpot API
    @GetMapping("/fetch-data")
    public void handleCallRecords() {
        // get call records from the api
        CallRecords callRecords = hubspotApiService.getCallRecords();
        // process the call records
        Results results = hubspotApiService.process(callRecords);
        // post the results of the processing to the api
        hubspotApiService.postResults(results);
    }
    
}