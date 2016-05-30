/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.model;

/**
 *
 * @author juan
 */
public class Territory {

    private String territory_name;
    private String territory_color;
    private TerritoryData territory;
    private String territory_id;

    /**
     * @return the territory_name
     */
    public String getTerritory_name() {
        return territory_name;
    }

    /**
     * @param territory_name the territory_name to set
     */
    public void setTerritory_name(String territory_name) {
        this.territory_name = territory_name;
    }

    /**
     * @return the territory_color
     */
    public String getTerritory_color() {
        return territory_color;
    }

    /**
     * @param territory_color the territory_color to set
     */
    public void setTerritory_color(String territory_color) {
        this.territory_color = territory_color;
    }

    /**
     * @return the territory
     */
    public TerritoryData getTerritory() {
        return territory;
    }

    /**
     * @param territory the territory to set
     */
    public void setTerritory(TerritoryData territory) {
        this.territory = territory;
    }

    /**
     * @return the territory_id
     */
    public String getTerritory_id() {
        return territory_id;
    }

    /**
     * @param territory_id the territory_id to set
     */
    public void setTerritory_id(String territory_id) {
        this.territory_id = territory_id;
    }

}
