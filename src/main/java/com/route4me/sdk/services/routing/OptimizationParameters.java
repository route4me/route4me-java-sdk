package com.route4me.sdk.services.routing;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OptimizationParameters {
    private transient String problemId;
    private transient boolean reoptimize;
    private transient boolean showDirections;
    private Parameters parameters;
    private List<Address> addresses;
}
