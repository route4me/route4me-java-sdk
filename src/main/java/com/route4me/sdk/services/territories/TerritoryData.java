
package com.route4me.sdk.services.territories;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 *
 * @author juan
 */
@Data
public class TerritoryData {

    @SerializedName("type")
    private String type;
    @SerializedName("data")
    private List<String> data;
}
