package com.route4me.sdk.services.users;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
/** The body of {@link TeamManagementManagerV5#bulkCreateTeamMembers(TeamMemberRequest[], String)}. */
@Data
public class BulkMembersRequest {
    /** The members to create, from 1 to 100 ({@code users}). */
    @SerializedName("users") private TeamMemberRequest[] users;
    /**
     * What to do with a member whose email address already exists: {@code fail}, {@code overwrite} or {@code skip}
     * ({@code conflicts}).
     */
    @SerializedName("conflicts") private String conflicts;
}
