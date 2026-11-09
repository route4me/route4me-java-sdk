package com.route4me.sdk.services.routing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Constants}: constants added in 2.0.0 come after the 1.16.1 ones, so
 * stored ordinals keep their meaning.
 */
public class ConstantsTest {

    @Test
    public void travelModeKeepsThe116Ordinals() {
        assertEquals(0, Constants.TravelMode.DRIVING.ordinal());
        assertEquals(1, Constants.TravelMode.WALKING.ordinal());
        assertEquals(2, Constants.TravelMode.TRUCKING.ordinal());
        assertEquals(3, Constants.TravelMode.BICYCLING.ordinal());
        assertEquals(4, Constants.TravelMode.values().length);
    }
}
