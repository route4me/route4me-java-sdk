package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** A page of POD workflows, the answer of {@link PodWorkflowManager#getPodWorkflows(Integer, String, String)}. */
@Data
public class PodWorkflowsResponse {
    /** The workflows of this page ({@code data}). */
    @SerializedName("data")
    private List<PodWorkflow> data;

    /**
     * The cursor of the next page, to pass as {@code cursor}; {@code null} on the last page ({@code next_page_cursor}).
     */
    @SerializedName("next_page_cursor")
    private String nextPageCursor;

    /** The number of workflows in all the pages ({@code total_items_count}). */
    @SerializedName("total_items_count")
    private Integer totalItemsCount;
}
