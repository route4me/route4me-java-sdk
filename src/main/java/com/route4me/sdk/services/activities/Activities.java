package com.route4me.sdk.services.activities;

import lombok.Data;

import java.util.List;

/** The answer of {@link ActivityManager#getActivities(ActivityRequest)}. */
@Data
public class Activities {

    /** The activities. */
    private List<Activity> results;
    /** The number of activities that match the request. */
    private Number total;
}
