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
public class Contact {
    private Number address_id;
    private String address_1;
    private String address_2;
    private String address_alias;
    private String address_group;
    private String first_name;
    private String last_name;
    private String address_email;
    private String address_phone_number;
    private Number cached_lat;
    private Number cached_lng;
    private String address_city;
    private String address_state_id;	
    private String address_country_id;	
    private String address_zip;	
    private List<Contact> results;

    /**
     * @return the address_1
     */
    public String getAddress_1() {
        return address_1;
    }

    /**
     * @param address_1 the address_1 to set
     */
    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    /**
     * @return the address_2
     */
    public String getAddress_2() {
        return address_2;
    }

    /**
     * @param address_2 the address_2 to set
     */
    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    /**
     * @return the address_alias
     */
    public String getAddress_alias() {
        return address_alias;
    }

    /**
     * @param address_alias the address_alias to set
     */
    public void setAddress_alias(String address_alias) {
        this.address_alias = address_alias;
    }

    /**
     * @return the address_group
     */
    public String getAddress_group() {
        return address_group;
    }

    /**
     * @param address_group the address_group to set
     */
    public void setAddress_group(String address_group) {
        this.address_group = address_group;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the address_email
     */
    public String getAddress_email() {
        return address_email;
    }

    /**
     * @param address_email the address_email to set
     */
    public void setAddress_email(String address_email) {
        this.address_email = address_email;
    }

    /**
     * @return the address_phone_number
     */
    public String getAddress_phone_number() {
        return address_phone_number;
    }

    /**
     * @param address_phone_number the address_phone_number to set
     */
    public void setAddress_phone_number(String address_phone_number) {
        this.address_phone_number = address_phone_number;
    }

    /**
     * @return the cached_lat
     */
    public Number getCached_lat() {
        return cached_lat;
    }

    /**
     * @param cached_lat the cached_lat to set
     */
    public void setCached_lat(Number cached_lat) {
        this.cached_lat = cached_lat;
    }

    /**
     * @return the cached_lng
     */
    public Number getCached_lng() {
        return cached_lng;
    }

    /**
     * @param cached_lng the cached_lng to set
     */
    public void setCached_lng(Number cached_lng) {
        this.cached_lng = cached_lng;
    }

    /**
     * @return the address_city
     */
    public String getAddress_city() {
        return address_city;
    }

    /**
     * @param address_city the address_city to set
     */
    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    /**
     * @return the address_state_id
     */
    public String getAddress_state_id() {
        return address_state_id;
    }

    /**
     * @param address_state_id the address_state_id to set
     */
    public void setAddress_state_id(String address_state_id) {
        this.address_state_id = address_state_id;
    }

    /**
     * @return the address_country_id
     */
    public String getAddress_country_id() {
        return address_country_id;
    }

    /**
     * @param address_country_id the address_country_id to set
     */
    public void setAddress_country_id(String address_country_id) {
        this.address_country_id = address_country_id;
    }

    /**
     * @return the address_zip
     */
    public String getAddress_zip() {
        return address_zip;
    }

    /**
     * @param address_zip the address_zip to set
     */
    public void setAddress_zip(String address_zip) {
        this.address_zip = address_zip;
    }

    /**
     * @return the address_id
     */
    public Number getAddress_id() {
        return address_id;
    }

    /**
     * @param address_id the address_id to set
     */
    public void setAddress_id(Number address_id) {
        this.address_id = address_id;
    }

    /**
     * @return the results
     */
    public List<Contact> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<Contact> results) {
        this.results = results;
    }
}
