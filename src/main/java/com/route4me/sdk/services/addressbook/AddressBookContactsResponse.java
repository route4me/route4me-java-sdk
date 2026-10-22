package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class AddressBookContactsResponse {
    @SerializedName("results") private List<AddressBookContactV5> results;
    @SerializedName("total") private Integer total;
}
