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
    private static final String SETTINGS_BY_MEMBER_EP = DRIVER_REVIEW_EP + "/settings/get-by-member";
    private static final String SETTINGS_UPLOAD_EP = DRIVER_REVIEW_EP + "/settings/upload";

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
     * review as the JSON body. {@link #patchDriverReview(String, DriverReview)} sends the same change as
     * {@code PATCH}; the spec and the C# SDK ({@code UpdateDriverReview}) take both.
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

    /**
     * Changes some fields of a driver review: {@code PATCH
     * https://wh.route4me.com/modules/api/v5.0/driver-reviews/{ratingId}}, with the review as the JSON body.
     *
     * @param ratingId the review ID
     * @param review the fields to change ({@code tracking_number}, {@code rating}, {@code review}); fields left
     *     {@code null} are not sent
     * @return the updated review
     * @throws IllegalArgumentException if {@code ratingId} is {@code null} or empty, or {@code review} is {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewResponse patchDriverReview(String ratingId, DriverReview review) throws APIException {
        if (review == null) {
            throw new IllegalArgumentException("The review must not be null");
        }
        URIBuilder builder = v5Builder(DRIVER_REVIEW_EP, ratingId);
        return this.makeRequest(RequestMethod.PATCH, builder, this.gson.toJson(review), DriverReviewResponse.class);
    }

    /**
     * Gets the settings of the review page of the account: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/driver-reviews/settings/get-by-member}.
     *
     * <p>The public variant of this call, {@code GET .../driver-reviews/settings/get?tracking_number=...}, needs no
     * API key and serves the review page itself, so the SDK does not offer it.
     *
     * @return the logo path, the question and the logo background color
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewSettingsResponse getDriverReviewSettingsByMember() throws APIException {
        return this.makeRequest(RequestMethod.GET, v5Builder(SETTINGS_BY_MEMBER_EP), (HttpEntity) null,
                DriverReviewSettingsResponse.class);
    }

    /**
     * Uploads the settings of the review page of the account: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/driver-reviews/settings/upload}, with the settings as the JSON body.
     * The logo goes Base64-encoded in {@code logo_picture}, as the spec says.
     *
     * @param settings the logo, the question and the logo background color
     * @return the stored settings, with the path of the logo
     * @throws IllegalArgumentException if {@code settings} is {@code null}
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public DriverReviewSettingsResponse uploadDriverReviewSettings(DriverReviewSettingsRequest settings)
            throws APIException {
        if (settings == null) {
            throw new IllegalArgumentException("The settings must not be null");
        }
        return this.makeRequest(RequestMethod.POST, v5Builder(SETTINGS_UPLOAD_EP), this.gson.toJson(settings),
                DriverReviewSettingsResponse.class);
    }
}
