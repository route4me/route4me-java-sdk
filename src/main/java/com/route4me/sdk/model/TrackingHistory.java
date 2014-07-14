package com.route4me.sdk.model;

/**
 *
 * @author juan
 */
public class TrackingHistory {
private Number s;
private Number lt; 
private Number lg;
private String d;            
private String ts;            
private String ts_friendly;     

    /**
     * @return the s
     */
    public Number getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(Number s) {
        this.s = s;
    }

    /**
     * @return the lt
     */
    public Number getLt() {
        return lt;
    }

    /**
     * @param lt the lt to set
     */
    public void setLt(Number lt) {
        this.lt = lt;
    }

    /**
     * @return the lg
     */
    public Number getLg() {
        return lg;
    }

    /**
     * @param lg the lg to set
     */
    public void setLg(Number lg) {
        this.lg = lg;
    }

    /**
     * @return the d
     */
    public String getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(String d) {
        this.d = d;
    }

    /**
     * @return the ts
     */
    public String getTs() {
        return ts;
    }

    /**
     * @param ts the ts to set
     */
    public void setTs(String ts) {
        this.ts = ts;
    }

    /**
     * @return the ts_friendly
     */
    public String getTs_friendly() {
        return ts_friendly;
    }

    /**
     * @param ts_friendly the ts_friendly to set
     */
    public void setTs_friendly(String ts_friendly) {
        this.ts_friendly = ts_friendly;
    }
}
