package com.route4me.sdk.responses;

import lombok.Data;
import java.util.List;

@Data
public class ErrorResponse {
    private List<String> errors;
}
