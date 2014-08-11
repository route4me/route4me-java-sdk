package com.route4me.sdk.model;

import java.util.List;

public class Routes {
	
	private List<Address> addresses;
	
	public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

}
