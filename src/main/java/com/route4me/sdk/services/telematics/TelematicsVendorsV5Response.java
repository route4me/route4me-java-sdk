package com.route4me.sdk.services.telematics;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

/**
 * The answer of {@link TelematicsManager#getTelematicsVendorsV5(TelematicsVendorsV5Request)}:
 * {@code {"data": [...]}}.
 */
@Data
public class TelematicsVendorsV5Response {

    /** The vendors that match the filters. */
    @SerializedName("data")
    private List<TelematicsVendorInfo> data;
}
