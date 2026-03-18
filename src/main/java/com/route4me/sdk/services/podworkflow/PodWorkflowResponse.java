package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PodWorkflowResponse {
    @SerializedName("data")
    private PodWorkflow data;
}
