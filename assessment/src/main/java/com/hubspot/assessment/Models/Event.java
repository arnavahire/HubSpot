package com.hubspot.assessment.Models;

/*
 * Event - Model that is used to store an event of the call. 
 * An event is starting of a call or ending of a call. So the event will store the timestamp of the call, if it is the start of the call
 * or end of the call using isStart variable and the callRecord object
 */
public class Event {
    long timestamp;
    boolean isStart;
    CallRecord callRecord;

    public Event(long timestamp, boolean isStart, CallRecord callRecord){
        this.timestamp = timestamp;
        this.isStart = isStart;
        this.callRecord = callRecord;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public boolean getIsStart(){
        return this.isStart;
    }

    public void setIsStart(boolean isStart){
        this.isStart = isStart;
    }

    public CallRecord getCallRecord(){
        return this.callRecord;
    }

    public void setCallRecord(CallRecord callRecord){
        this.callRecord = callRecord;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                ", isStart=" + isStart +
                ", callRecord=" + callRecord +
                '}';
    }

}
