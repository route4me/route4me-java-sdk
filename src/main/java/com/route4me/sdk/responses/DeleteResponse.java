package com.route4me.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/** The answer of the V4 delete methods: {@code {"status": true}} when something was deleted. */
@Data
public class DeleteResponse {
    /** Whether something was deleted ({@code status}). */
    @SerializedName("status")
    private boolean deleted;
}
