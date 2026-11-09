/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.territories;

import java.awt.Polygon;

/** A territory and its polygon, from {@link TerritoriesManager#getPolygonsFromTerritories()}. */
public class TerritoryPolygon {

    private Polygon polygon;
    private Territory territory;

    TerritoryPolygon(Territory territory, Polygon polygon) {
        this.territory = territory;
        this.polygon = polygon;
    }

    /**
     * Returns the polygon of the territory.
     *
     * @return the polygon, in coordinates multiplied by 1,000,000
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * Sets the polygon of the territory.
     *
     * @param polygon the polygon, in coordinates multiplied by 1,000,000
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    /**
     * Returns the territory.
     *
     * @return the territory
     */
    public Territory getTerritory() {
        return territory;
    }

    /**
     * Sets the territory.
     *
     * @param territory the territory
     */
    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

}
