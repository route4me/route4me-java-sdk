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
public class Notes {
    private Boolean status;
    private String note_id;
    private String upload_id;
    private Note note;

    /**
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
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
     * @return the note
     */
    public Note getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(Note note) {
        this.note = note;
    }



    
}
