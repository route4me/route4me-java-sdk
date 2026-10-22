package com.route4me.sdk.services.users;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/** The answer of {@link UsersManager#getUsers()}: the users of the account. */
@Data
public class UsersResponse {
    /** The users ({@code results}). */
    @SerializedName("results")
    private List<User> results;
}
