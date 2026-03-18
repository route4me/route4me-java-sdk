package com.route4me.sdk.services.users;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class BulkMembersRequest {
    @SerializedName("users") private TeamMemberRequest[] users;
    @SerializedName("conflicts") private String conflicts;
}
