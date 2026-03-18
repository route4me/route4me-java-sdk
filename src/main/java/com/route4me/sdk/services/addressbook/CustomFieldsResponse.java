package com.route4me.sdk.services.addressbook;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class CustomFieldsResponse {
    @SerializedName("data") private List<String> data;
}
