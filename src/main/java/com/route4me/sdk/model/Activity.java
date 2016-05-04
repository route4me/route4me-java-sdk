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
public class Activity {

    private String activity_id;
    private String activity_type;
    private String activity_timestamp;
    private String activity_message;
    private String route_id;
    private String route_name;
    private String route_destination_id;
    private String note_id;
    private String note_type;
    private String note_contents;
    private String note_file;
    private User member;

    /**
     * @return the activity_id
     */
    public String getActivity_id() {
        return activity_id;
    }

    /**
     * @param activity_id the activity_id to set
     */
    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    /**
     * @return the activity_type
     */
    public String getActivity_type() {
        return activity_type;
    }

    /**
     * @param activity_type the activity_type to set
     */
    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    /**
     * @return the activity_timestamp
     */
    public String getActivity_timestamp() {
        return activity_timestamp;
    }

    /**
     * @param activity_timestamp the activity_timestamp to set
     */
    public void setActivity_timestamp(String activity_timestamp) {
        this.activity_timestamp = activity_timestamp;
    }

    /**
     * @return the activity_message
     */
    public String getActivity_message() {
        return activity_message;
    }

    /**
     * @param activity_message the activity_message to set
     */
    public void setActivity_message(String activity_message) {
        this.activity_message = activity_message;
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
     * @return the route_name
     */
    public String getRoute_name() {
        return route_name;
    }

    /**
     * @param route_name the route_name to set
     */
    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    /**
     * @return the route_destination_id
     */
    public String getRoute_destination_id() {
        return route_destination_id;
    }

    /**
     * @param route_destination_id the route_destination_id to set
     */
    public void setRoute_destination_id(String route_destination_id) {
        this.route_destination_id = route_destination_id;
    }

    /**
     * @return the note_id
     */
    public String getNote_id() {
        return note_id;
    }

    /**
     * @param note_id the note_id to set
     */
    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    /**
     * @return the note_type
     */
    public String getNote_type() {
        return note_type;
    }

    /**
     * @param note_type the note_type to set
     */
    public void setNote_type(String note_type) {
        this.note_type = note_type;
    }

    /**
     * @return the note_contents
     */
    public String getNote_contents() {
        return note_contents;
    }

    /**
     * @param note_contents the note_contents to set
     */
    public void setNote_contents(String note_contents) {
        this.note_contents = note_contents;
    }

    /**
     * @return the note_file
     */
    public String getNote_file() {
        return note_file;
    }

    /**
     * @param note_file the note_file to set
     */
    public void setNote_file(String note_file) {
        this.note_file = note_file;
    }

    /**
     * @return the member
     */
    public User getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(User member) {
        this.member = member;
    }
    
}
