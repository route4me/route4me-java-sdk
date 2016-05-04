/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.model;

import java.util.List;

/**
 *
 * @author juan
 */
public class Activities {
    private List<Activity> results;
    private Number total;

    /**
     * @return the results
     */
    public List<Activity> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<Activity> results) {
        this.results = results;
    }

    /**
     * @return the total
     */
    public Number getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Number total) {
        this.total = total;
    }
    
}
