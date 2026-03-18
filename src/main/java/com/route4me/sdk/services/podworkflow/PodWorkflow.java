package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class PodWorkflow {
    @SerializedName("workflow_guid")
    private String workflowGuid;

    @SerializedName("workflow_id")
    private String workflowId;

    @SerializedName("root_member_id")
    private Long rootMemberId;

    @SerializedName("is_enabled")
    private Boolean isEnabled;

    @SerializedName("is_default")
    private Boolean isDefault;

    @SerializedName("title")
    private String title;

    @SerializedName("done_actions")
    private List<Object> doneActions;

    @SerializedName("failed_actions")
    private List<Object> failedActions;
}
