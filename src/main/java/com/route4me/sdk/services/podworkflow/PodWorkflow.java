package com.route4me.sdk.services.podworkflow;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * A proof-of-delivery workflow: the actions that complete a stop ({@code done_actions}) and those that fail it
 * ({@code failed_actions}).
 */
@Data
public class PodWorkflow {
    /** Unique GUID of the workflow ({@code workflow_guid}). */
    @SerializedName("workflow_guid")
    private String workflowGuid;

    /** Unique workflow identifier ({@code workflow_id}). */
    @SerializedName("workflow_id")
    private String workflowId;

    /** The member ID of the account owner ({@code root_member_id}). */
    @SerializedName("root_member_id")
    private Long rootMemberId;

    /** Indicates whether the workflow is enabled ({@code is_enabled}). */
    @SerializedName("is_enabled")
    private Boolean isEnabled;

    /** Marks this workflow as the default one ({@code is_default}). */
    @SerializedName("is_default")
    private Boolean isDefault;

    /** Display title of the workflow ({@code title}). */
    @SerializedName("title")
    private String title;

    /** List of required actions to complete the workflow ({@code done_actions}). */
    @SerializedName("done_actions")
    private List<Object> doneActions;

    /** List of possible failure actions for the workflow ({@code failed_actions}). */
    @SerializedName("failed_actions")
    private List<Object> failedActions;
}
