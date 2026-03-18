package com.route4me.sdk.services.accountprofile;

import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class AccountProfileManager extends Manager {

    private static final String V5_BASE = "/modules/api/v5.0";
    private static final String ACCOUNT_PROFILE_EP = V5_BASE + "/profile-api";

    public AccountProfileManager(String apiKey) {
        super(apiKey);
    }

    public AccountProfileManager(String apiKey, boolean disableRedirects) {
        super(apiKey, disableRedirects);
    }

    private static URIBuilder v5Builder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("wh.route4me.com");
        builder.setPath(endpoint);
        return builder;
    }

    public AccountProfile getAccountProfile() throws APIException {
        URIBuilder builder = v5Builder(ACCOUNT_PROFILE_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, AccountProfile.class);
    }
}
