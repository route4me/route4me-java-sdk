package com.route4me.sdk.services.driverreview;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the reviews that customers give drivers, through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0/driver-reviews}). The account needs the
 * driver rating module; without it the API answers 403.
 */
public class DriverReviewManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String DRIVER_REVIEW_EP = V5_BASE + "/driver-reviews";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public DriverReviewManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public DriverReviewManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /**
     * Lists driver reviews, with filters, one page at a time: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/driver-reviews}. Every {@code null} argument is left out
     * of the query.
     *
     * @param rating only reviews with this rating ({@code rating})
     * @param memberId only reviews of this driver ({@code member_id})
     * @param start only reviews added on or after this date ({@code start})
     * @param end only reviews added on or before this date ({@code end})
     * @param page the page number, from 1 ({@code page})
     * @param perPage the number of reviews per page ({@code per_page})
     * @return the page of reviews, the links to the other pages and the number of reviews per rating
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewsResponse getDriverReviews(Integer rating, Long memberId, String start, String end,
            Integer page, Integer perPage) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP);
        if (rating != null) {
            builder.setParameter("rating", String.valueOf(rating));
        }
        if (memberId != null) {
            builder.setParameter("member_id", String.valueOf(memberId));
        }
        if (start != null) {
            builder.setParameter("start", start);
        }
        if (end != null) {
            builder.setParameter("end", end);
        }
        if (page != null) {
            builder.setParameter("page", String.valueOf(page));
        }
        if (perPage != null) {
            builder.setParameter("per_page", String.valueOf(perPage));
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, DriverReviewsResponse.class);
    }

    /**
     * Gets one driver review: {@code GET https://wh.route4me.com/modules/api/v5.0/driver-reviews/{ratingId}}.
     *
     * @param ratingId the review ID
     * @return the review
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewResponse getDriverReviewById(String ratingId) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP, ratingId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, DriverReviewResponse.class);
    }

    /**
     * Adds a driver review: {@code POST https://wh.route4me.com/modules/api/v5.0/driver-reviews}, with the review as
     * the JSON body.
     *
     * @param review the tracking number, the rating and the text
     * @return the added review
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewResponse createDriverReview(DriverReview review) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP);
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(review), DriverReviewResponse.class);
    }

    /**
     * Updates a driver review: {@code PUT https://wh.route4me.com/modules/api/v5.0/driver-reviews/{ratingId}}, with the
     * review as the JSON body.
     *
     * @param ratingId the review ID
     * @param review the new values of the review
     * @return the updated review
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewResponse updateDriverReview(String ratingId, DriverReview review) throws APIException {
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP, ratingId);
        return this.makeRequest(RequestMethod.PUT, builder, this.gson.toJson(review), DriverReviewResponse.class);
    }
}
