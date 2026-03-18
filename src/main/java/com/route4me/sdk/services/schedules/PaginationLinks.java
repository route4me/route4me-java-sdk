package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PaginationLinks {
    @SerializedName("first")
    private String first;

    @SerializedName("last")
    private String last;

    @SerializedName("prev")
    private String prev;

    @SerializedName("next")
    private String next;
}
