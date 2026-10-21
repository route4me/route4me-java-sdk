package com.route4me.sdk;

import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.services.accountprofile.AccountProfile;
import com.route4me.sdk.services.locations.LocationCombinedDataItem;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.telematics.TelematicsConnection;
import com.route4me.sdk.services.telematics.TelematicsMemberRegister;
import com.route4me.sdk.services.users.Registration;
import com.route4me.sdk.services.users.TeamMemberRequest;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UserRegistration;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Checks that credentials do not leak through {@code toString()} or exception messages,
 * where they end up in logs.
 *
 * <p>Each test asserts the safe behaviour. The ones that fail today are ignored with a
 * {@code security:} reason, so a fix only has to remove the {@code @Ignore}.
 */
public class ApiKeyExposureTest {

    /** A made-up password or token; only its presence in the output matters. */
    private static final String PASSWORD = "example-password-value";

    @Rule
    public final MockHttp http = new MockHttp();

    /** Fails when {@code text} contains {@code secret}. */
    private static void assertHidden(String what, String text, String secret) {
        assertFalse(what + " must not contain the secret value", text.contains(secret));
    }

    // ------------------------------------------------------------------ toString()

    @Ignore("security: User.toString() (Lombok @Data) prints api_key and member_password")
    @Test
    public void userToStringHidesCredentials() {
        User user = new User();
        user.setMemberEmail("alex@example.com");
        user.setApiKey(MockHttp.API_KEY);
        user.setMemberPassword(PASSWORD);

        assertHidden("User.toString()", user.toString(), MockHttp.API_KEY);
        assertHidden("User.toString()", user.toString(), PASSWORD);
    }

    @Ignore("security: AccountProfile.toString() (Lombok @Data) prints api_key")
    @Test
    public void accountProfileToStringHidesTheApiKey() {
        AccountProfile profile = new AccountProfile();
        profile.setEmail("owner@example.com");
        profile.setApiKey(MockHttp.API_KEY);

        assertHidden("AccountProfile.toString()", profile.toString(), MockHttp.API_KEY);
    }

    @Ignore("security: TelematicsConnection.toString() (Lombok @Data) prints the vendor api_key and password")
    @Test
    public void telematicsConnectionToStringHidesCredentials() {
        TelematicsConnection connection = new TelematicsConnection();
        connection.setUsername("fleet-user");
        connection.setApiKey(MockHttp.API_KEY);
        connection.setPassword(PASSWORD);

        assertHidden("TelematicsConnection.toString()", connection.toString(), MockHttp.API_KEY);
        assertHidden("TelematicsConnection.toString()", connection.toString(), PASSWORD);
    }

    @Ignore("security: Registration.toString() (Lombok @Data) prints api_key")
    @Test
    public void registrationToStringHidesTheApiKey() {
        Registration registration = new Registration();
        registration.setMemberEmail("alex@example.com");
        registration.setApiKey(MockHttp.API_KEY);

        assertHidden("Registration.toString()", registration.toString(), MockHttp.API_KEY);
    }

    @Ignore("security: UserRegistration.toString() (Lombok @Data) prints organization_api_key and passwords")
    @Test
    public void userRegistrationToStringHidesCredentials() {
        UserRegistration registration = new UserRegistration();
        registration.setStrEmail("alex@example.com");
        registration.setOrganizationApiKey(MockHttp.API_KEY);
        registration.setStrPassword_1(PASSWORD);
        registration.setStrPassword_2(PASSWORD);

        assertHidden("UserRegistration.toString()", registration.toString(), MockHttp.API_KEY);
        assertHidden("UserRegistration.toString()", registration.toString(), PASSWORD);
    }

    @Ignore("security: TeamMemberRequest.toString() (Lombok @Data) prints member_password")
    @Test
    public void teamMemberRequestToStringHidesThePassword() {
        TeamMemberRequest request = new TeamMemberRequest();
        request.setMemberEmail("driver@example.com");
        request.setMemberPassword(PASSWORD);

        assertHidden("TeamMemberRequest.toString()", request.toString(), PASSWORD);
    }

    @Ignore("security: TelematicsMemberRegister.toString() (Lombok @Data) prints the telematics api_token")
    @Test
    public void telematicsMemberRegisterToStringHidesTheToken() {
        TelematicsMemberRegister registration = new TelematicsMemberRegister();
        registration.setTelematicRegisterID("REG-1");
        registration.setApiToken(PASSWORD);

        assertHidden("TelematicsMemberRegister.toString()", registration.toString(), PASSWORD);
    }

    @Ignore("security: LocationCombinedDataItem.toString() (Lombok @Data) prints external_connection_token")
    @Test
    public void locationItemToStringHidesTheConnectionToken() {
        LocationCombinedDataItem item = new LocationCombinedDataItem();
        item.setAddress1("100 Example St");
        item.setExternalConnectionToken(PASSWORD);

        assertHidden("LocationCombinedDataItem.toString()", item.toString(), PASSWORD);
    }

    // ------------------------------------------------------------------ exception messages

    @Test
    public void errorStatusMessageDoesNotEchoTheApiKey() {
        final RoutingManager manager = http.install(new RoutingManager(MockHttp.API_KEY));
        http.respond(401, "{\"errors\":[\"Invalid api_key " + MockHttp.API_KEY + "\"]}");

        APIException error = http.expectApiException(() -> manager.getRoute(new RoutesRequest()));

        assertHidden("APIException message", error.getMessage(), MockHttp.API_KEY);
        assertHidden("APIException errors", error.getErrors().toString(), MockHttp.API_KEY);
        assertHidden("APIException body", error.getResponseBody(), MockHttp.API_KEY);
        assertHidden("APIException URL", error.getUrl(), MockHttp.API_KEY);
    }

    @Test
    public void parseErrorMessageDoesNotEchoTheResponse() {
        final RoutingManager manager = http.install(new RoutingManager(MockHttp.API_KEY));
        http.respond(200, "{\"api_key\":\"" + MockHttp.API_KEY + "\"}");

        APIException error = http.expectApiException(() -> manager.getRoutes(new RoutesRequest()));

        assertHidden("APIException message", error.getMessage(), MockHttp.API_KEY);
        assertHidden("APIException message", error.getMessage(), "api_key\"");
        assertHidden("APIException body", error.getResponseBody(), MockHttp.API_KEY);
    }
}
