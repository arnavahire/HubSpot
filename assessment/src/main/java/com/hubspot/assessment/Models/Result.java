package com.hubspot.assessment.Models;

import java.util.List;

/*
 * Result - Model to hold the single object within the final result which will be sent via POST request
 */
public class Result {
    private int customerId;
    private String date;
    private int maxConcurrentCalls;
    private long timestamp;
    private List<String> callIds;

    public Result(){

    }

    public Result(int customerId, String date, int maxConcurrentCalls, long timestamp, List<String> callIds) {
        this.customerId = customerId;
        this.date = date;
        this.maxConcurrentCalls = maxConcurrentCalls;
        this.timestamp = timestamp;
        this.callIds = callIds;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // Getter and Setter for date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter and Setter for maxConcurrentCalls
    public int getMaxConcurrentCalls() {
        return maxConcurrentCalls;
    }

    public void setMaxConcurrentCalls(int maxConcurrentCalls) {
        this.maxConcurrentCalls = maxConcurrentCalls;
    }

    // Getter and Setter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Getter and Setter for callIds
    public List<String> getCallIds() {
        return callIds;
    }

    public void setCallIds(List<String> callIds) {
        this.callIds = callIds;
    }

    public String toString(){
        return "Result{" +
        "customerId=" + customerId +
        ", date='" + date + '\'' +
        ", maxConcurrentCalls=" + maxConcurrentCalls +
        ", timestamp=" + timestamp +
        ", callIds=" + callIds +
        '}';
    }
}
