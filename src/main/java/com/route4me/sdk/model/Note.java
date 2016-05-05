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
public class Note {
    private String route_destination_id;
    private Number note_id;
    private String upload_extension;
    private String route_id;
    private String upload_id;
    private String upload_type;
    private String device_type;
    private Number lat;
    private Number lng;
    private String upload_url;
    private Number ts_added;
    private String contents;
    private String activity_type;   

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
    public Number getNote_id() {
        return note_id;
    }

    /**
     * @param note_id the note_id to set
     */
    public void setNote_id(Number note_id) {
        this.note_id = note_id;
    }

    /**
     * @return the upload_extension
     */
    public String getUpload_extension() {
        return upload_extension;
    }

    /**
     * @param upload_extension the upload_extension to set
     */
    public void setUpload_extension(String upload_extension) {
        this.upload_extension = upload_extension;
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
     * @return the upload_id
     */
    public String getUpload_id() {
        return upload_id;
    }

    /**
     * @param upload_id the upload_id to set
     */
    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    /**
     * @return the upload_type
     */
    public String getUpload_type() {
        return upload_type;
    }

    /**
     * @param upload_type the upload_type to set
     */
    public void setUpload_type(String upload_type) {
        this.upload_type = upload_type;
    }

    /**
     * @return the device_type
     */
    public String getDevice_type() {
        return device_type;
    }

    /**
     * @param device_type the device_type to set
     */
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
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
     * @return the upload_url
     */
    public String getUpload_url() {
        return upload_url;
    }

    /**
     * @param upload_url the upload_url to set
     */
    public void setUpload_url(String upload_url) {
        this.upload_url = upload_url;
    }

    /**
     * @return the ts_added
     */
    public Number getTs_added() {
        return ts_added;
    }

    /**
     * @param ts_added the ts_added to set
     */
    public void setTs_added(Number ts_added) {
        this.ts_added = ts_added;
    }

    /**
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
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
}
