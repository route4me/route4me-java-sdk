package com.route4me.sdk.services.users;

import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.Manager;
import com.route4me.sdk.RequestMethod;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.StatusResponse;
import static com.route4me.sdk.queryconverter.QueryConverter.convertObjectToParameters;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

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
    /** The path that checks a member session, on {@code api.route4me.com}. */
    public static final String VALIDATE_SESSION_EP = "/datafeed/session/validate_session.php";
    /** The path of the capabilities of the account, on {@code api.route4me.com}. */
    public static final String CAPABILITIES_EP = "/api/member/capabilities.php";
    /** The path that records a user license bought in a mobile app, on {@code api.route4me.com}. */
    public static final String USER_LICENSE_EP = "/api/member/user_license.php";
    /** The path that registers a person for a webinar, on {@code api.route4me.com}. */
    public static final String WEBINAR_REGISTER_EP = "/actions/webinar_register.php";

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
     * @deprecated the V4 {@code /api.v4/user.php} endpoint is obsolete: the C# SDK marks {@code CreateUser} as
     *     {@code [Obsolete]}. Use {@link TeamManagementManagerV5#createTeamMember(TeamMemberRequest)}. This method
     *     will be removed in the next major version.
     */
    @Deprecated
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
     * @deprecated the V4 {@code /api.v4/user.php} endpoint is obsolete: the C# SDK marks {@code UserUpdate} as
     *     {@code [Obsolete]}. Use {@link TeamManagementManagerV5#updateTeamMember(long, TeamMemberRequest)}. This
     *     method will be removed in the next major version.
     */
    @Deprecated
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
     * @deprecated the V4 {@code /api.v4/user.php} endpoint is obsolete: the C# SDK marks {@code GetUserById} as
     *     {@code [Obsolete]}. Use {@link TeamManagementManagerV5#getTeamMemberById(long)}. This method will be
     *     removed in the next major version.
     */
    @Deprecated
    public User getUser(String memberId) throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        builder.setParameter("member_id", memberId);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, User.class);
    }

    /**
     * Deletes a user of the account: {@code DELETE https://api.route4me.com/api.v4/user.php?member_id=...}, as the C#
     * SDK ({@code UserDelete}) sends it. The V5 API does the same with
     * {@link TeamManagementManagerV5#deleteTeamMember(long)}.
     *
     * @param memberId the member ID of the user
     * @return {@code status} {@code true} when the user was deleted
     * @throws IllegalArgumentException if {@code memberId} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the V4 {@code /api.v4/user.php} endpoint is obsolete: the C# SDK marks {@code UserDelete} as
     *     {@code [Obsolete]}. Use {@link TeamManagementManagerV5#deleteTeamMember(long)}. This method will be removed
     *     in the next major version.
     */
    @Deprecated
    public StatusResponse userDelete(String memberId) throws APIException {
        requireText(memberId, "memberId");
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        builder.setParameter("member_id", memberId);
        return this.makeRequest(RequestMethod.DELETE, builder, (HttpEntity) null, StatusResponse.class);
    }

    /**
     * Lists the users of the account: {@code GET https://api.route4me.com/api.v4/user.php}, without a member ID, as
     * the C# SDK ({@code GetUsers}) sends it. {@link #getUsers(int, int)} reads the users, one page at a time, from
     * another V4 endpoint, and the V5 API lists them with {@link TeamManagementManagerV5#getTeamMembers()}.
     *
     * @return the users
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     * @deprecated the V4 {@code /api.v4/user.php} endpoint is obsolete: the C# SDK marks {@code GetUsers} as
     *     {@code [Obsolete]}. Use {@link TeamManagementManagerV5#getTeamMembers()}. This method will be removed in
     *     the next major version.
     */
    @Deprecated
    public UsersResponse getUsers() throws APIException {
        URIBuilder builder = Manager.defaultBuilder(USER_EP);
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, UsersResponse.class);
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
     * Logs a member in with an email address and a password: {@code POST
     * https://api.route4me.com/actions/authenticate.php}, with {@code strEmail}, {@code strPassword} and
     * {@code format=json} as a form, as the C# SDK ({@code UserAuthentication}) sends them.
     *
     * <p>The password travels only in the request body. The answer holds the API key and the session of the member:
     * do not log them.
     *
     * @param email the email address of the member
     * @param password the password of the member
     * @return the session, the member ID and the API key of the member
     * @throws IllegalArgumentException if {@code email} or {@code password} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MemberResponse userAuthentication(String email, String password) throws APIException {
        requireText(email, "email");
        requireText(password, "password");
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("strEmail", email));
        form.add(new BasicNameValuePair("strPassword", password));
        form.add(new BasicNameValuePair("format", "json"));
        return this.makeRequest(RequestMethod.POST, Manager.defaultBuilder(AUTHENTICATE_EP), form,
                MemberResponse.class);
    }

    /**
     * Checks whether a member session is still valid: {@code GET
     * https://api.route4me.com/datafeed/session/validate_session.php?session_guid=...&member_id=...&format=json}, as
     * the C# SDK ({@code ValidateSession}) sends it.
     *
     * <p>The session GUID is a credential: it travels in the query, as the API requires, and the SDK hides it in its
     * exception messages.
     *
     * @param sessionGuid the session GUID, from {@link #userAuthentication(String, String)}
     * @param memberId the member ID of the session
     * @return the session, with {@code authenticated} {@code true} when it is valid
     * @throws IllegalArgumentException if {@code sessionGuid} is {@code null} or empty
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MemberResponse validateSession(String sessionGuid, long memberId) throws APIException {
        requireText(sessionGuid, "sessionGuid");
        URIBuilder builder = Manager.defaultBuilder(VALIDATE_SESSION_EP);
        builder.setParameter("session_guid", sessionGuid);
        builder.setParameter("member_id", Long.toString(memberId));
        builder.setParameter("format", "json");
        return this.makeRequest(RequestMethod.GET, builder, (HttpEntity) null, MemberResponse.class);
    }

    /**
     * Gets the capabilities of the account: {@code GET https://api.route4me.com/api/member/capabilities.php}, as the
     * C# SDK ({@code GetMemberCapabilities}) sends it, without its second copy of the API key.
     *
     * @return the features, travel modes and licensed modules of the account
     * @throws APIException if the request fails, the server answers with an error status or the answer cannot be parsed
     */
    public MemberCapabilities getMemberCapabilities() throws APIException {
        return this.makeRequest(RequestMethod.GET, Manager.defaultBuilder(CAPABILITIES_EP), (HttpEntity) null,
                MemberCapabilities.class);
    }

    /**
     * Records a user license bought in a mobile app: {@code POST
     * https://api.route4me.com/api/member/user_license.php?device_id=...}, with the license as the JSON body. The
     * PHP SDK ({@code purchaseUserLicense}) sends the device ID in the query and in the body, and so does this
     * method. No spec or C# method documents the endpoint or its answer, so the answer is returned as text.
     *
     * @param license the member, its session, the device, the subscription and the purchase token and receipt
     * @return the answer body, as the API sends it (JSON)
     * @throws IllegalArgumentException if {@code license} is {@code null}
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String purchaseUserLicense(UserLicenseRequest license) throws APIException {
        if (license == null) {
            throw new IllegalArgumentException("The license must not be null");
        }
        URIBuilder builder = Manager.defaultBuilder(USER_LICENSE_EP);
        if (license.getDeviceId() != null) {
            builder.setParameter("device_id", license.getDeviceId());
        }
        return this.makeRequest(RequestMethod.POST, builder, this.gson.toJson(license), String.class);
    }

    /**
     * Registers a person for a webinar: {@code POST https://api.route4me.com/actions/webinar_register.php}, with the
     * registration as the JSON body, as the PHP SDK ({@code webinarRegistration}) sends it. No spec or C# method
     * documents the endpoint or its answer, so the answer is returned as text.
     *
     * @param registration the person, the company, the member ID and the webinar date
     * @return the answer body, as the API sends it
     * @throws IllegalArgumentException if {@code registration} is {@code null}
     * @throws APIException if the request fails or the server answers with an error status
     */
    public String registerForWebinar(WebinarRegistration registration) throws APIException {
        if (registration == null) {
            throw new IllegalArgumentException("The registration must not be null");
        }
        return this.makeRequest(RequestMethod.POST, Manager.defaultBuilder(WEBINAR_REGISTER_EP),
                this.gson.toJson(registration), String.class);
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

    /** Rejects a {@code null} or empty required argument before any request is sent. */
    private static void requireText(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The " + name + " must not be null or empty");
        }
    }
}
