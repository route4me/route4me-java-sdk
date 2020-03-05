/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.route4me.sdk.services.routing;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 *
 * @author juan
 */

@Data
public class OptimizationProblemIDs {
    @SerializedName("optimization_problem_ids")
    private List<String> optimizationProblemIDs;
    
    
}
