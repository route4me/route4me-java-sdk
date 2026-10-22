package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
@Data
public class ContactIdsRequest {
    @SerializedName("address_ids") private long[] addressIds;
}
