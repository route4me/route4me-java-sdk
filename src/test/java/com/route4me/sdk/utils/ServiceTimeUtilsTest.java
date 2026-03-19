package com.route4me.sdk.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceTimeUtilsTest {

    @Test
    public void testExistingKeyReturnsLongValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("delivery", 300);
        Long result = ServiceTimeUtils.getServiceTimeByAddressType("delivery", map);
        assertNotNull(result);
        assertEquals(Long.valueOf(300L), result);
    }

    @Test
    public void testMissingKeyReturnsNull() {
        Map<String, Integer> map = new HashMap<>();
        map.put("delivery", 300);
        Long result = ServiceTimeUtils.getServiceTimeByAddressType("pickup", map);
        assertNull(result);
    }

    @Test
    public void testEmptyMapReturnsNull() {
        Map<String, Integer> map = new HashMap<>();
        Long result = ServiceTimeUtils.getServiceTimeByAddressType("delivery", map);
        assertNull(result);
    }

    @Test
    public void testMultipleKeysReturnsCorrectValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("delivery", 300);
        map.put("pickup", 600);
        map.put("service", 900);
        assertEquals(Long.valueOf(600L), ServiceTimeUtils.getServiceTimeByAddressType("pickup", map));
    }
}
