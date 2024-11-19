package com.hubspot.assessment.Models;

/*
 * ClassRecord - Model for Storing each call record coming from the HubSpot API response
 */
public class CallRecord{
    // keep vairables private by default and make them accessible only through getters and setters
    private int customerId;
    private String callId;
    private long startTimestamp;
    private long endTimestamp;

    // Default constructor - required by Jackson in order to map a string to object
    public CallRecord() {}

    public CallRecord(int customerId, String callId, long startTimestamp, long endTimestamp){
        this.customerId = customerId;
        this.callId = callId;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    // override toString
    @Override
    public String toString() {
        return "CallRecord{" +
                "customerId=" + customerId +
                ", callId='" + callId + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                '}';
    }
}
