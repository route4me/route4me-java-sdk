package com.route4me.sdk.services.schedules;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The links to the other pages of a paged list. */
@Data
public class PaginationLinks {
    /** The URL of the first page ({@code first}). */
    @SerializedName("first")
    private String first;

    /** The URL of the last page ({@code last}). */
    @SerializedName("last")
    private String last;

    /** The URL of the previous page, or {@code null} on the first page ({@code prev}). */
    @SerializedName("prev")
    private String prev;

    /** The URL of the next page, or {@code null} on the last page ({@code next}). */
    @SerializedName("next")
    private String next;
}
