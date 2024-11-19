package com.hubspot.assessment.Models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 *  CallRecords - Stores the complete object received from HubSpot API as json.
 */
public class CallRecords {
    @JsonProperty("callRecords") // Map the field name to the JSON field because jackson by default maps to actual values, so here instead of records we should have callrecords to map fields as they are
    List<CallRecord> records;

    public List<CallRecord> getRecords(){
        return records;
    }

    public void setRecords(List<CallRecord> records){
        this.records = records;
    }
    
}
