package com.route4me.sdk.services.users;

import com.route4me.sdk.RequestMethod;
import com.google.gson.Gson;
import com.route4me.sdk.Manager;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.model.enums.APIEndpoints;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;

public class UsersManager extends Manager {

    public UsersManager(String apiKey) {
        super(apiKey, new Gson());
    }

    public List<User> getUsers(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(APIEndpoints.GET_USERS_HOST);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offest", Integer.toString(offset));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<User>>() {
        }.getType());
    }

}
