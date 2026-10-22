/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.queryconverter.QueryParameter;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
// equals and hashCode compare the fields of this class only, as they always have.
@EqualsAndHashCode(callSuper = false)
public class Optimization extends DataObject {
    @SerializedName("routes")
    @QueryParameter("routes")
    private List<Route> routes;


}
