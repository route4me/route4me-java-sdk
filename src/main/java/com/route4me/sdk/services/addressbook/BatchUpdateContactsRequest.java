package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class BatchUpdateContactsRequest {
    @SerializedName("data") private List<AddressBookContactV5> data;
}
