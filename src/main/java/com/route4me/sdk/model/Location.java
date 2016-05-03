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
public class Location {
    private Number time;
    private Number segment_distance;
    private String name;
    private String start_location;
    private String end_location;
    private String directions_error;
    private Number error_code;

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

    /**
     * @return the segment_distance
     */
    public Number getSegment_distance() {
        return segment_distance;
    }

    /**
     * @param segment_distance the segment_distance to set
     */
    public void setSegment_distance(Number segment_distance) {
        this.segment_distance = segment_distance;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the start_location
     */
    public String getStart_location() {
        return start_location;
    }

    /**
     * @param start_location the start_location to set
     */
    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    /**
     * @return the end_location
     */
    public String getEnd_location() {
        return end_location;
    }

    /**
     * @param end_location the end_location to set
     */
    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    /**
     * @return the directions_error
     */
    public String getDirections_error() {
        return directions_error;
    }

    /**
     * @param directions_error the directions_error to set
     */
    public void setDirections_error(String directions_error) {
        this.directions_error = directions_error;
    }

    /**
     * @return the error_code
     */
    public Number getError_code() {
        return error_code;
    }

    /**
     * @param error_code the error_code to set
     */
    public void setError_code(Number error_code) {
        this.error_code = error_code;
    }
    
}
