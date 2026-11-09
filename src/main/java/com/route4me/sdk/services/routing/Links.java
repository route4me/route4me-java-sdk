package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.utils.Sanitizer;
import lombok.Data;

/**
 * The URLs of an optimization and of its view.
 *
 * <p>The V4 API may put the {@code api_key} in these URLs, so {@link #toString()} hides
 * credential query parameters (see {@link Sanitizer#redactUrl(String)}). The getters return
 * the URLs unchanged.
 */
@Data
public class Links {

    /** The URL of the optimization ({@code optimization_problem}). */
    @SerializedName("optimization_problem")
    private String optimizationProblem;
    /** API URL Route GET call for the current route ({@code route}). */
    @SerializedName("route")
    private String route;
    /** A Link to the GET operation for the optimization problem ({@code view}). */
    @SerializedName("view")
    private String view;

    /**
     * Returns the URLs with the values of credential query parameters, such as
     * {@code api_key}, replaced by {@link Sanitizer#REDACTED}, so the result is safe to log.
     *
     * @return the three URLs, with credentials hidden
     */
    @Override
    public String toString() {
        return "Links(optimizationProblem=" + Sanitizer.redactUrl(this.optimizationProblem)
                + ", route=" + Sanitizer.redactUrl(this.route)
                + ", view=" + Sanitizer.redactUrl(this.view) + ")";
    }
}
