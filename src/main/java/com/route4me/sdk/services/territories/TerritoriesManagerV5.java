package com.route4me.sdk.services.territories;

import com.route4me.sdk.AsyncJob;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

/**
 * Reads territories and avoidance zones, and clusters addresses into territories, through the V5 API ({@code
 * https://wh.route4me.com/modules/api/v5.0}). {@link TerritoriesManager} covers the V4 API.
 */
public class TerritoriesManagerV5 extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String TERRITORIES_EP = V5_BASE + "/territories";
    private static final String AVOIDANCE_EP = V5_BASE + "/avoidance";
    private static final String AUTOMATIC_TERRITORIES_EP = V5_BASE + "/automatic-territories";
    private static final String AUTOMATIC_JOB_STATUS_EP = AUTOMATIC_TERRITORIES_EP + "/job-tracker/status";
    private static final String AUTOMATIC_JOB_RESULT_EP = AUTOMATIC_TERRITORIES_EP + "/job-tracker/result";

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     */
    public TerritoriesManagerV5(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a manager that sends its requests with an API key, and may refuse redirects.
     *
     * @param apiKey the Route4Me API key, sent as a bearer token
     * @param disableRedirects {@code true} to add {@code redirect=0} to every request and not follow redirects
     */
    public TerritoriesManagerV5(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint, Object... segments) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        Manager.withPathSegments(builder, endpoint, segments);
        return builder;
    }

    /** Checks that a latitude and a longitude are given together or not at all, as the spec requires. */
    private static void requireBothOrNeither(Object lat, Object lng, String latName, String lngName) {
        if ((lat == null) != (lng == null)) {
            throw new IllegalArgumentException(latName + " and " + lngName + " must be given together");
        }
    }

    /**
     * Lists the territories: {@code GET https://wh.route4me.com/modules/api/v5.0/territories}, with the query.
     *
     * @param query the point, dates, search text and what to return with each territory; {@code null} lists all
     * @return the territories
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if only one of {@code lat} and {@code lng} is given
     */
    public TerritoryResults getTerritories(TerritoriesQuery query) throws APIException {
        if (query != null) {
            requireBothOrNeither(query.getLat(), query.getLng(), "lat", "lng");
        }
        URIBuilder builder = v5Builder(TERRITORIES_EP);
        return this.makeJSONRequest(RequestMethod.GET, builder, query, TerritoryResults.class);
    }

    /**
     * Lists the avoidance zones: {@code GET https://wh.route4me.com/modules/api/v5.0/avoidance}. Every {@code null}
     * argument is left out of the query.
     *
     * @param positionLat only the zones around this point: its latitude, given with {@code positionLng} ({@code
     *                    position_lat})
     * @param positionLng the longitude of that point ({@code position_lng})
     * @param distance the radius around that point ({@code distance})
     * @param searchQuery text to search the zones for ({@code search_query})
     * @return the avoidance zones
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if only one of {@code positionLat} and {@code positionLng} is given
     */
    public TerritoryResults getAvoidanceZones(Double positionLat, Double positionLng, Double distance,
            String searchQuery) throws APIException {
        requireBothOrNeither(positionLat, positionLng, "position_lat", "position_lng");
        URIBuilder builder = v5Builder(AVOIDANCE_EP);
        if (positionLat != null) {
            builder.setParameter("position_lat", String.valueOf(positionLat));
            builder.setParameter("position_lng", String.valueOf(positionLng));
        }
        if (distance != null) {
            builder.setParameter("distance", String.valueOf(distance));
        }
        if (searchQuery != null) {
            builder.setParameter("search_query", searchQuery);
        }
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, TerritoryResults.class);
    }

    /**
     * Starts a job that clusters addresses into territories: {@code POST
     * https://wh.route4me.com/modules/api/v5.0/automatic-territories}, with the request as the JSON body.
     *
     * <p>The API accepts the job with {@code 202 Accepted} and puts the job ID in the {@code X-job-id} response
     * header, and the status URL in {@code Location}. Follow the job with
     * {@link #getAutomaticTerritoriesJobStatus(String)}, then read the clusters with
     * {@link #getAutomaticTerritoriesJobResult(String)}:
     * <pre>{@code
     * AsyncJob job = manager.createAutomaticTerritoriesJob(request);
     * AutomaticTerritoriesJobStatus status = manager.getAutomaticTerritoriesJobStatus(job.getJobId());
     * }</pre>
     *
     * @param request the addresses or the temporary address storage, the mode and the parameters
     * @return the accepted job: its ID, status URL and reply body (empty, or {@code {}} or {@code []})
     * @throws APIException if the request fails or the server answers with an error status
     * @throws IllegalArgumentException if the request is {@code null}, has neither addresses nor a storage ID, or has
     *                                  an address without an ID
     */
    public AsyncJob createAutomaticTerritoriesJob(AutomaticTerritoriesJobRequest request) throws APIException {
        if (request == null) {
            throw new IllegalArgumentException("The request is required");
        }
        boolean noStorage = request.getTemporaryAddressesStorageId() == null
                || request.getTemporaryAddressesStorageId().isEmpty();
        boolean noAddresses = request.getAddresses() == null || request.getAddresses().isEmpty();
        if (noStorage && noAddresses) {
            throw new IllegalArgumentException("Give the addresses or a temporary_addresses_storage_id");
        }
        if (!noAddresses) {
            for (AutomaticTerritoriesAddress address : request.getAddresses()) {
                if (address == null || address.getId() == null) {
                    throw new IllegalArgumentException("Every address needs an id");
                }
            }
        }
        URIBuilder builder = v5Builder(AUTOMATIC_TERRITORIES_EP);
        return this.makeJobRequest(RequestMethod.POST, builder, this.gson.toJson(request));
    }

    /**
     * Gets the status of an automatic territories job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/automatic-territories/job-tracker/status/{jobId}}.
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of
     *              {@link #createAutomaticTerritoriesJob(AutomaticTerritoriesJobRequest)}
     * @return the status of the job
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @throws IllegalArgumentException if the job ID is {@code null}, empty, {@code .} or {@code ..}
     */
    public AutomaticTerritoriesJobStatus getAutomaticTerritoriesJobStatus(String jobId) throws APIException {
        URIBuilder builder = v5Builder(AUTOMATIC_JOB_STATUS_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AutomaticTerritoriesJobStatus.class);
    }

    /**
     * Gets the clusters of a finished automatic territories job: {@code GET
     * https://wh.route4me.com/modules/api/v5.0/automatic-territories/job-tracker/result/{jobId}}.
     *
     * @param jobId the job ID, such as {@link AsyncJob#getJobId()} of
     *              {@link #createAutomaticTerritoriesJob(AutomaticTerritoriesJobRequest)}
     * @return the clusters
     * @throws APIException if the request fails, the server answers with an error status (409 while the job runs) or
     *                      the answer cannot be parsed
     * @throws IllegalArgumentException if the job ID is {@code null}, empty, {@code .} or {@code ..}
     */
    public AutomaticTerritoriesJobResult getAutomaticTerritoriesJobResult(String jobId) throws APIException {
        URIBuilder builder = v5Builder(AUTOMATIC_JOB_RESULT_EP, jobId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AutomaticTerritoriesJobResult.class);
    }
}
