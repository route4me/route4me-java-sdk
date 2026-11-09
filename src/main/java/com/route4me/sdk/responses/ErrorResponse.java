package com.route4me.sdk.responses;

import lombok.Data;
import java.util.List;

/** A V4 error answer: {@code {"errors": [...]}}. */
@Data
public class ErrorResponse {
    /** The error messages. */
    private List<String> errors;
}
