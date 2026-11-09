package com.route4me.sdk.services.tracking;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/** The answer of {@link TrackingManager#setTrackingPings(TrackingPingsRequest)}, for one ping or a batch. */
@Data
public class TrackingPingsResponse {
    /** For one ping, whether it was stored; for a batch, whether at least one item was ({@code status}). */
    @SerializedName("status")
    private Boolean status;
    /** The transaction ID of a stored single ping ({@code tx_id}). */
    @SerializedName("tx_id")
    private String txId;
    /** Why a single ping was rejected, when the API says ({@code message}). */
    @SerializedName("message")
    private String message;
    /** The time of a single ping, in Unix seconds, with {@code extend_response_data} ({@code activity_at}). */
    @SerializedName("activity_at")
    private Long activityAt;
    /** Whether the device is a phone or tablet, with {@code extend_response_data} ({@code is_mobile}). */
    @SerializedName("is_mobile")
    private Boolean isMobile;
    /** The speed in mph, with {@code extend_response_data} ({@code mph_speed}). */
    @SerializedName("mph_speed")
    private Double mphSpeed;
    /** Why no item of a batch was stored, when the reason applies to the whole batch ({@code errors}). */
    @SerializedName("errors")
    private List<String> errors;
    /** The rejected items of a batch ({@code items_errors}). */
    @SerializedName("items_errors")
    private List<ItemError> itemsErrors;

    /** A rejected item of a batch. */
    @Data
    public static class ItemError {
        /** The index of the item in {@code items}, from 0 ({@code index}). */
        @SerializedName("index")
        private Integer index;
        /** Why the item was rejected ({@code message}). */
        @SerializedName("message")
        private String message;
    }
}
