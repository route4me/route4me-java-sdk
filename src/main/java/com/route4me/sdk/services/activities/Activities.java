package com.route4me.sdk.services.activities;

import lombok.Data;

import java.util.List;

@Data
public class Activities {

    private List<Activity> results;
    private Number total;
}
