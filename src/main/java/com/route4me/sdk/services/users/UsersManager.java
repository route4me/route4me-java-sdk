package com.route4me.sdk.services.users;

import com.route4me.sdk.RequestMethod;
import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.exception.APIException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class UsersManager extends Manager {
    public static final String USERS_EP = "/api/member/view_users.php";

    public UsersManager(String apiKey) {
        super(apiKey);
    }

    public List<User> getUsers(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USERS_EP);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offset", Integer.toString(offset));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<User>>() {
        }.getType());
    }

}
