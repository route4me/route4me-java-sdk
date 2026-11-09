package com.route4me.sdk.services.driverreview;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** A review that a customer gave the driver of a delivery. */
@Data
public class DriverReview {
    /** The ID of the review ({@code rating_id}). */
    @SerializedName("rating_id")
    private String ratingId;

    /** The tracking number of the destination the review is about ({@code tracking_number}). */
    @SerializedName("tracking_number")
    private String trackingNumber;

    /** The text of the review ({@code review}). */
    @SerializedName("review")
    private String review;

    /** The rating the customer gave ({@code rating}). */
    @SerializedName("rating")
    private Double rating;

    /** The date the review was added, such as {@code 2020-03-27} ({@code added_at}). */
    @SerializedName("added_at")
    private String addedAt;
}
