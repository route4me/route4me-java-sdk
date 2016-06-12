package com.route4me.sdk.model;

import lombok.Data;

import java.util.List;

@Data
public class Direction {

    private Location location;
    private List<Step> steps;
}
