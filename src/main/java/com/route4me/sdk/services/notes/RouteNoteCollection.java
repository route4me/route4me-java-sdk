package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
@Data
public class RouteNoteCollection {
    @SerializedName("data") private List<RouteNoteResource> data;
    @SerializedName("current_page") private Integer currentPage;
    @SerializedName("total") private Integer total;
    @SerializedName("per_page") private Integer perPage;
}
