package com.route4me.sdk.services.notes;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;
/** A page of notes, as the list methods of {@link NotesManagerV5} return it. */
@Data
public class RouteNoteCollection {
    /** Array of note resources ({@code data}). */
    @SerializedName("data") private List<RouteNoteResource> data;
    /** Current page number ({@code current_page}). */
    @SerializedName("current_page") private Integer currentPage;
    /** Total count of notes ({@code total}). */
    @SerializedName("total") private Integer total;
    /** Items per page ({@code per_page}). */
    @SerializedName("per_page") private Integer perPage;
}
