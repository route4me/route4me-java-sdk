package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class PodWorkflowsResponse {
    @SerializedName("data")
    private List<PodWorkflow> data;

    @SerializedName("next_page_cursor")
    private String nextPageCursor;

    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
