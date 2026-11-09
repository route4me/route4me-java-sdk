package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the single-workflow methods of {@link PodWorkflowManager}: {@code {"data": {...}}}. */
@Data
public class PodWorkflowResponse {
    /** The workflow ({@code data}). */
    @SerializedName("data")
    private PodWorkflow data;
}
