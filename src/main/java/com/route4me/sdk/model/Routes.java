package com.route4me.sdk.model;

import java.util.List;

public class Routes {
	
	private List<Address> addresses;
	private Parameters parameters;
        private String route_id;
        private String optimization_problem_id; 
        private String member_id;
        
        
        
        
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

}
