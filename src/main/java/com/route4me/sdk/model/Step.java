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
public class Step {
    private String directions;
    private String direction;
    private Number distance;
    private String distance_unit;
    private String maneuverType;
    private String compass_direction;
    private ManeuverPoint maneuverPoint;
    private Number duration_sec;  

    /**
     * @return the directions
     */
    public String getDirections() {
        return directions;
    }

    /**
     * @param directions the directions to set
     */
    public void setDirections(String directions) {
        this.directions = directions;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the distance
     */
    public Number getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Number distance) {
        this.distance = distance;
    }

    /**
     * @return the distance_unit
     */
    public String getDistance_unit() {
        return distance_unit;
    }

    /**
     * @param distance_unit the distance_unit to set
     */
    public void setDistance_unit(String distance_unit) {
        this.distance_unit = distance_unit;
    }

    /**
     * @return the maneuverType
     */
    public String getManeuverType() {
        return maneuverType;
    }

    /**
     * @param maneuverType the maneuverType to set
     */
    public void setManeuverType(String maneuverType) {
        this.maneuverType = maneuverType;
    }

    /**
     * @return the compass_direction
     */
    public String getCompass_direction() {
        return compass_direction;
    }

    /**
     * @param compass_direction the compass_direction to set
     */
    public void setCompass_direction(String compass_direction) {
        this.compass_direction = compass_direction;
    }

    /**
     * @return the maneuverPoint
     */
    public ManeuverPoint getManeuverPoint() {
        return maneuverPoint;
    }

    /**
     * @param maneuverPoint the maneuverPoint to set
     */
    public void setManeuverPoint(ManeuverPoint maneuverPoint) {
        this.maneuverPoint = maneuverPoint;
    }

    /**
     * @return the duration_sec
     */
    public Number getDuration_sec() {
        return duration_sec;
    }

    /**
     * @param duration_sec the duration_sec to set
     */
    public void setDuration_sec(Number duration_sec) {
        this.duration_sec = duration_sec;
    }
}
