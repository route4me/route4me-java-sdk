/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.territories;

import java.awt.Polygon;

/**
 *
 * @author juan
 */
public class TerritoryPolygon {
    
    private Polygon polygon;
    private Territory territory;

    TerritoryPolygon(Territory territory, Polygon polygon) {
        this.territory = territory;
        this.polygon = polygon;
    }

    /**
     * @return the polygon
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @param polygon the polygon to set
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    /**
     * @return the territory
     */
    public Territory getTerritory() {
        return territory;
    }

    /**
     * @param territory the territory to set
     */
    public void setTerritory(Territory territory) {
        this.territory = territory;
    }
    
}
