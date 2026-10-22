package com.route4me.sdk.services.users;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import static com.route4me.sdk.queryconverter.QueryConverter.convertObjectToParameters;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

/**
 * Manages the users of the account and registers new accounts, through the V4 API ({@code https://api.route4me.com}).
 */
public class UsersManager extends Manager {

    /** The path that lists the users of the account, on {@code api.route4me.com}. */
    public static final String USERS_EP = "/api/member/view_users.php";
    /** The path of one user, on {@code api.route4me.com}. */
    public static final String USER_EP = "/api.v4/user.php";
    /** The path that authenticates a user, on {@code api.route4me.com}. */
    public static final String AUTHENTICATE_EP = "/actions/authenticate.php";
    /** The path that registers a new account, on {@code api.route4me.com}. */
    public static final String REGISTER_EP = "/actions/register_action.php";

    /**
     * Creates a manager without an API key, to register a new account with
     * {@link #registerUser(UserRegistration)}.
     */
    public UsersManager() {
        super(null);
    }

    /**
     * Creates a manager that sends its requests with an API key.
     *
     * @param apiKey the Route4Me API key, sent as the {@code api_key} query parameter
     */
    public UsersManager(String apiKey) {
        super(apiKey);
    }

    /**
     * Creates a user of the account: {@code POST https://api.route4me.com/api.v4/user.php}, with the user as the JSON
     * body.
     *
     * @param user the user
     * @return the created user
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public User createUser(User user) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        return this.makeJSONRequest(RequestMethod.POST, builder, user, User.class);
    }

    /**
     * Updates a user of the account: {@code PUT https://api.route4me.com/api.v4/user.php}, with the user as the JSON
     * body.
     *
     * @param user the user, with its member ID
     * @return the updated user
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public User updateUser(User user) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        return this.makeJSONRequest(RequestMethod.PUT, builder, user, User.class);
    }

    /**
     * Gets one user: {@code GET https://api.route4me.com/api.v4/user.php?member_id=...}.
     *
     * @param memberId the member ID
     * @return the user
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public User getUser(String memberId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        builder.setParameter("member_id", memberId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, User.class);
    }

    /**
     * Lists the users of the account: {@code GET
     * https://api.route4me.com/api/member/view_users.php?limit=...&offset=...}.
     *
     * @param limit the maximum number of users
     * @param offset the number of users to skip
     * @return the users
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public List<User> getUsers(int limit, int offset) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USERS_EP);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("offset", Integer.toString(offset));
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, new TypeToken<ArrayList<User>>() {
        }.getType());
    }

    /**
     * Registers a new account: {@code POST https://api.route4me.com/actions/register_action.php?plan=...}, with the
     * registration as a form.
     *
     * @param register the user, the company and the plan
     * @return the new account, with its API key; do not log it
     * @throws IllegalAccessException if the registration cannot be read for the form
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public Registration registerUser(UserRegistration register) throws IllegalAccessException, APIException {
        URIBuilder builder = Manager.defaultBuilder(REGISTER_EP);
        builder.setParameter("plan", register.getPlan());
        List<NameValuePair> body = convertObjectToParameters(register);
        return this.makeRequest(RequestMethod.POST, builder, body, Registration.class);

    }

}
