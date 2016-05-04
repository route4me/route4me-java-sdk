package com.route4me.sdk.model;

import java.util.List;

public class Routes {
	
	private List<Address> addresses;
	private Parameters parameters;
        private String route_id;
        private String optimization_problem_id; 
        private String member_id;
        private String member_email;
        private List<Direction> directions;
        private String driver_alias;
        private String vehicle_alias;
        private Boolean deleted;
        private List<String> routes_ids;
        private List<String> tracking_history;
        
        
        
        
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
     * @return the route_id
     */
    public String getRoute_id() {
        return route_id;
    }

    /**
     * @param route_id the route_id to set
     */
    public void setRoute_id(String route_id) {
        this.route_id = route_id;
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
     * @return the member_id
     */
    public String getMember_id() {
        return member_id;
    }

    /**
     * @param member_id the member_id to set
     */
    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    /**
     * @return the member_email
     */
    public String getMember_email() {
        return member_email;
    }

    /**
     * @param member_email the member_email to set
     */
    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    /**
     * @return the directions
     */
    public List<Direction> getDirections() {
        return directions;
    }

    /**
     * @param directions the directions to set
     */
    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    /**
     * @return the driver_alias
     */
    public String getDriver_alias() {
        return driver_alias;
    }

    /**
     * @param driver_alias the driver_alias to set
     */
    public void setDriver_alias(String driver_alias) {
        this.driver_alias = driver_alias;
    }

    /**
     * @return the vehicle_alias
     */
    public String getVehicle_alias() {
        return vehicle_alias;
    }

    /**
     * @param vehicle_alias the vehicle_alias to set
     */
    public void setVehicle_alias(String vehicle_alias) {
        this.vehicle_alias = vehicle_alias;
    }

    /**
     * @return the deleted
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the routes_ids
     */
    public List<String> getRoutes_ids() {
        return routes_ids;
    }

    /**
     * @param routes_ids the routes_ids to set
     */
    public void setRoutes_ids(List<String> routes_ids) {
        this.routes_ids = routes_ids;
    }

    /**
     * @return the tracking_history
     */
    public List<String> getTracking_history() {
        return tracking_history;
    }

    /**
     * @param tracking_history the tracking_history to set
     */
    public void setTracking_history(List<String> tracking_history) {
        this.tracking_history = tracking_history;
    }

}
