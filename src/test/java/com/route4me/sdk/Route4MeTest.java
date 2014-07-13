/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.route4me.sdk;

import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;


/**
 *
 * @author khepri
 */
public class Route4MeTest {
    
    private static Route4Me route4me;
    private static volatile String apiKey;
    
    public Route4MeTest() {
    }
    
    @BeforeClass
    public static void setUp() {
        apiKey = "11111111111111111111111111111111";
        route4me = new Route4Me(apiKey);
    }
    
    @Test
    public void testAPIKeySet(){
        assertEquals(apiKey, route4me.getOptimization().getParams().get("api_key"));
    }
    
}
