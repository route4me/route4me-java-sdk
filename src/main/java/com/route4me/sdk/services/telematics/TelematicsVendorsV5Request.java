package com.route4me.sdk.services.telematics;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Optional filters of {@link TelematicsManager#getTelematicsVendorsV5(TelematicsVendorsV5Request)}.
 * Every field left {@code null} is left out of the request.
 */
@Data
@Accessors(chain = true)
public class TelematicsVendorsV5Request {

    /** Text to look for in the vendor name ({@code search}). */
    private String search;
    /** A country code the vendor must serve ({@code country}). */
    private String country;
    /** One feature the vendor must offer ({@code feature}). */
    private String feature;
    /** Features the vendor must offer, sent as {@code features[]}. */
    private List<String> features;
    /** The vendor size, such as {@code global} ({@code size}). */
    private String size;
    /** Only vendors that are, or are not, integrated with Route4Me ({@code is_integrated}). */
    private Boolean isIntegrated;
    /** Whether to include hidden vendors ({@code display_hidden}). */
    private Boolean displayHidden;
    /** The page number, from 1 ({@code page}). */
    private Integer page;
    /** The number of vendors per page, from 1 to 500 ({@code per_page}). */
    private Integer perPage;
}
