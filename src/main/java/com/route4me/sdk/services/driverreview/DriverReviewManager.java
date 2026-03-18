package com.route4me.sdk.services.driverreview;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class DriverReviewManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String DRIVER_REVIEW_EP = V5_BASE + "/driver-reviews";

    public DriverReviewManager(String apiKey) {
        super(apiKey);
    }

    public DriverReviewManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public DriverReviewsResponse getDriverReviews(Integer rating, Long memberId, String start, String end, Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP);
        if (rating != null) builder.setParameter("rating", String.valueOf(rating));
        if (memberId != null) builder.setParameter("member_id", String.valueOf(memberId));
        if (start != null) builder.setParameter("start", start);
        if (end != null) builder.setParameter("end", end);
        if (page != null) builder.setParameter("page", String.valueOf(page));
        if (perPage != null) builder.setParameter("per_page", String.valueOf(perPage));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, DriverReviewsResponse.class);
    }

    public DriverReviewResponse getDriverReviewById(String ratingId) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP + "/" + ratingId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, DriverReviewResponse.class);
    }

    public DriverReviewResponse createDriverReview(DriverReview review) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(review), DriverReviewResponse.class);
    }

    public DriverReviewResponse updateDriverReview(String ratingId, DriverReview review) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP + "/" + ratingId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(review), DriverReviewResponse.class);
    }
}
