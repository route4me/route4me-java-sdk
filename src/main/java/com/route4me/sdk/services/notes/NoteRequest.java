package com.route4me.sdk.services.notes;

import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.routing.Constants;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NoteRequest {
    @QueryParameter("route_id")
    private String routeId;
    @QueryParameter("address_id")
    private String addressId;
    @QueryParameter("dev_lat")
    private Double latitude;
    @QueryParameter("dev_lng")
    private Double longitude;
    @QueryParameter("device_type")
    private Constants.DeviceType deviceType;
}
