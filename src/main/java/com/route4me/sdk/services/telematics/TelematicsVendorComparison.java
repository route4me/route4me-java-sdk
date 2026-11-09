/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.telematics;

/**
 * The features of one vendor, as the deprecated {@link TelematicsManager#compareTelematicsVendors(String)} returns
 * them.
 */
public class TelematicsVendorComparison {

    private String name;
    private String id;
    private String features;

    @Override
    public String toString() {
        return "ID=" + this.id + " Name=" + this.name + " Features=" + this.features;
    }

    /**
     * Returns the name of the vendor.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the vendor.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the ID of the vendor.
     *
     * @return the vendor ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the vendor.
     *
     * @param id the vendor ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the features of the vendor.
     *
     * @return the feature names, separated by commas
     */
    public String getFeatures() {
        return features;
    }

    /**
     * Sets the features of the vendor.
     *
     * @param features the feature names, separated by commas
     */
    public void setFeatures(String features) {
        this.features = features;
    }

}
