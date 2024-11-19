package com.hubspot.assessment.Models;

import java.util.List;

/*
 * Results - Model to hold the final object which will be sent to HubSpot API in the POST request
 */
public class Results {
    private List<Result> results;

    public Results(List<Result> results){
        this.results = results;
    }

    public List<Result> getResults() {
        return this.results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String toString(){
        return "Results{" +
                "results=" + results +"}";
    }
}
