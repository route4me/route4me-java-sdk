package com.route4me.sdk.model;

import java.util.List;

/**
 *
 * @author Juan
 */
public class DataObject {

    private List<Address> addresses;
    private List<TrackingHistory> tracking_history;
    private Parameters parameters;
    private String optimization_problem_id; 
    private Number state;
    private Boolean sent_to_background;
    private Links links;
    private List<Routes> routes;
    private List<Optimizations> optimizations;
    
    
    /**
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the parameters
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the optimization_problem_id
     */
    public String getOptimization_problem_id() {
        return optimization_problem_id;
    }

    /**
     * @param optimization_problem_id the optimization_problem_id to set
     */
    public void setOptimization_problem_id(String optimization_problem_id) {
        this.optimization_problem_id = optimization_problem_id;
    }

    /**
     * @return the state
     */
    public Number getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.setState((Number) state);
    }

    /**
     * @return the sent_to_background
     */
    public Boolean getSent_to_background() {
        return sent_to_background;
    }

    /**
     * @param sent_to_background the sent_to_background to set
     */
    public void setSent_to_background(Boolean sent_to_background) {
        this.sent_to_background = sent_to_background;
    }

    /**
     * @return the links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * @return the tracking_history
     */
    public List<TrackingHistory> getTracking_history() {
        return tracking_history;
    }

    /**
     * @param tracking_history the tracking_history to set
     */
    public void setTracking_history(List<TrackingHistory> tracking_history) {
        this.tracking_history = tracking_history;
    }


    /**
     * @return the optimizations
     */
    public List<Optimizations> getOptimizations() {
        return optimizations;
    }

    /**
     * @param optimizations the optimizations to set
     */
    public void setOptimizations(List<Optimizations> optimizations) {
        this.optimizations = optimizations;
    }

    /**
     * @param state the state to set
     */
    public void setState(Number state) {
        this.state = state;
    }

    /**
     * @return the routes
     */
    public List<Routes> getRoutes() {
        return routes;
    }

    /**
     * @param routes the routes to set
     */
    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }
    
}
