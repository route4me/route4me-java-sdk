/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.model;

/**
 *
 * @author juan
 */
public class Address {

    private Number route_destination_id;
    private String alias;
    private Number member_id;
    private String address;
    private Boolean is_depot;
    private Number lat;
    private Number lng;
    private String route_id;
    private String original_route_id;
    private String optimization_problem_id;
    private Number sequence_no;
    private Boolean geocoded;
    private Number preferred_geocoding;
    private Boolean failed_geocoding;
    private Number contact_id;
    private Boolean is_visited;
    private Number destination_note_count;
    private String channel_name;
    private Number time_window_start;
    private Number time_window_end;
    private Number time;

    public Address() {
    }

    public Address(String address, Number lat, Number lng, Number time) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public Address(String address, Boolean is_depot, Number lat, Number lng, Number time) {
        this(address, lat, lng, time);
        this.is_depot = is_depot;
    }

    public Address(String address, Boolean is_depot, String alias, Number lat, Number lng, Number time) {
        this(address, is_depot, lat, lng, time);
        this.alias = alias;
    }

    public Address(String address, Boolean is_depot, Number lat, Number lng, Number time, Number time_window_start, Number time_window_end) {
        this.address = address;
        this.is_depot = is_depot;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.time_window_start = time_window_start;
        this.time_window_end = time_window_end;
    }

    public Address(String address, Number lat, Number lng, Number time, Number time_window_start, Number time_window_end) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.time_window_start = time_window_start;
        this.time_window_end = time_window_end;
    }

    public Address(String address, String alias, Number lat, Number lng, Number time) {
        this.alias = alias;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    /**
     * @return the route_destination_id
     */
    public Number getRoute_destination_id() {
        return route_destination_id;
    }

    /**
     * @param route_destination_id the route_destination_id to set
     */
    public void setRoute_destination_id(Number route_destination_id) {
        this.route_destination_id = route_destination_id;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the member_id
     */
    public Number getMember_id() {
        return member_id;
    }

    /**
     * @param member_id the member_id to set
     */
    public void setMember_id(Number member_id) {
        this.member_id = member_id;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the is_depot
     */
    public Boolean isIs_depot() {
        return is_depot;
    }

    /**
     * @param is_depot the is_depot to set
     */
    public void setIs_depot(Boolean is_depot) {
        this.is_depot = is_depot;
    }

    /**
     * @return the lat
     */
    public Number getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(Number lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public Number getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(Number lng) {
        this.lng = lng;
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
     * @return the original_route_id
     */
    public String getOriginal_route_id() {
        return original_route_id;
    }

    /**
     * @param original_route_id the original_route_id to set
     */
    public void setOriginal_route_id(String original_route_id) {
        this.original_route_id = original_route_id;
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
     * @return the sequence_no
     */
    public Number getSequence_no() {
        return sequence_no;
    }

    /**
     * @param sequence_no the sequence_no to set
     */
    public void setSequence_no(Number sequence_no) {
        this.sequence_no = sequence_no;
    }

    /**
     * @return the geocoded
     */
    public Boolean isGeocoded() {
        return geocoded;
    }

    /**
     * @param geocoded the geocoded to set
     */
    public void setGeocoded(Boolean geocoded) {
        this.geocoded = geocoded;
    }

    /**
     * @return the preferred_geocoding
     */
    public Number getPreferred_geocoding() {
        return preferred_geocoding;
    }

    /**
     * @param preferred_geocoding the preferred_geocoding to set
     */
    public void setPreferred_geocoding(Number preferred_geocoding) {
        this.preferred_geocoding = preferred_geocoding;
    }

    /**
     * @return the failed_geocoding
     */
    public Boolean isFailed_geocoding() {
        return failed_geocoding;
    }

    /**
     * @param failed_geocoding the failed_geocoding to set
     */
    public void setFailed_geocoding(Boolean failed_geocoding) {
        this.failed_geocoding = failed_geocoding;
    }

    /**
     * @return the contact_id
     */
    public Number getContact_id() {
        return contact_id;
    }

    /**
     * @param contact_id the contact_id to set
     */
    public void setContact_id(Number contact_id) {
        this.contact_id = contact_id;
    }

    /**
     * @return the is_visited
     */
    public Boolean isIs_visited() {
        return is_visited;
    }

    /**
     * @param is_visited the is_visited to set
     */
    public void setIs_visited(Boolean is_visited) {
        this.is_visited = is_visited;
    }

    /**
     * @return the destination_note_count
     */
    public Number getDestination_note_count() {
        return destination_note_count;
    }

    /**
     * @param destination_note_count the destination_note_count to set
     */
    public void setDestination_note_count(Number destination_note_count) {
        this.destination_note_count = destination_note_count;
    }

    /**
     * @return the channel_name
     */
    public String getChannel_name() {
        return channel_name;
    }

    /**
     * @param channel_name the channel_name to set
     */
    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    /**
     * @return the time_window_start
     */
    public Number getTime_window_start() {
        return time_window_start;
    }

    /**
     * @param time_window_start the time_window_start to set
     */
    public void setTime_window_start(Number time_window_start) {
        this.time_window_start = time_window_start;
    }

    /**
     * @return the time_window_end
     */
    public Number getTime_window_end() {
        return time_window_end;
    }

    /**
     * @param time_window_end the time_window_end to set
     */
    public void setTime_window_end(Number time_window_end) {
        this.time_window_end = time_window_end;
    }

    /**
     * @return the time
     */
    public Number getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Number time) {
        this.time = time;
    }

}
