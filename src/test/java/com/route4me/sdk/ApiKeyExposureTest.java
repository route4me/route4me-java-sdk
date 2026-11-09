package com.route4me.sdk;

import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryParameter;
import com.route4me.sdk.services.accountprofile.AccountProfile;
import com.route4me.sdk.services.configurationsettings.ConfigurationData;
import com.route4me.sdk.services.configurationsettings.MemberConfigurationSetting;
import com.route4me.sdk.services.locations.LocationCombinedDataItem;
import com.route4me.sdk.services.routing.RoutesRequest;
import com.route4me.sdk.services.routing.RoutingManager;
import com.route4me.sdk.services.telematics.TelematicsConnection;
import com.route4me.sdk.services.telematics.TelematicsMemberRegister;
import com.route4me.sdk.services.telematics.TelematicsVendor;
import com.route4me.sdk.services.telematics.TelematicsVendorV5;
import com.route4me.sdk.services.users.MemberResponse;
import com.route4me.sdk.services.users.Registration;
import com.route4me.sdk.services.users.TeamMemberRequest;
import com.route4me.sdk.services.users.User;
import com.route4me.sdk.services.users.UserLicenseRequest;
import com.route4me.sdk.services.users.UserRegistration;
import com.route4me.sdk.testsupport.MockHttp;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Checks that credentials do not leak through {@code toString()} or exception messages,
 * where they end up in logs.
 *
 * <p>Each test asserts the safe behaviour.
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

    @Test
    public void userToStringHidesCredentials() {
        User user = new User();
        user.setMemberEmail("alex@example.com");
        user.setApiKey(MockHttp.API_KEY);
        user.setMemberPassword(PASSWORD);

        assertHidden("User.toString()", user.toString(), MockHttp.API_KEY);
        assertHidden("User.toString()", user.toString(), PASSWORD);
    }

    @Test
    public void accountProfileToStringHidesTheApiKey() {
        AccountProfile profile = new AccountProfile();
        profile.setEmail("owner@example.com");
        profile.setApiKey(MockHttp.API_KEY);

        assertHidden("AccountProfile.toString()", profile.toString(), MockHttp.API_KEY);
    }

    @Test
    public void telematicsConnectionToStringHidesCredentials() {
        TelematicsConnection connection = new TelematicsConnection();
        connection.setUsername("fleet-user");
        connection.setApiKey(MockHttp.API_KEY);
        connection.setPassword(PASSWORD);
        connection.setConnectionToken("example-connection-token");

        assertHidden("TelematicsConnection.toString()", connection.toString(), MockHttp.API_KEY);
        assertHidden("TelematicsConnection.toString()", connection.toString(), PASSWORD);
        assertHidden("TelematicsConnection.toString()", connection.toString(), "example-connection-token");
        assertTrue("other fields are still shown", connection.toString().contains("fleet-user"));
    }

    @Test
    public void registrationToStringHidesTheApiKey() {
        Registration registration = new Registration();
        registration.setMemberEmail("alex@example.com");
        registration.setApiKey(MockHttp.API_KEY);
        registration.setSessionGUID("example-session-guid");
        registration.setSessionID("example-session-id");

        assertHidden("Registration.toString()", registration.toString(), MockHttp.API_KEY);
        assertHidden("Registration.toString()", registration.toString(), "example-session-guid");
        assertHidden("Registration.toString()", registration.toString(), "example-session-id");
    }

    @Test
    public void userRegistrationToStringHidesCredentials() {
        UserRegistration registration = new UserRegistration();
        registration.setStrEmail("alex@example.com");
        registration.setOrganizationApiKey(MockHttp.API_KEY);
        registration.setStrPassword_1(PASSWORD);
        registration.setStrPassword_2(PASSWORD);
        registration.setActivationKey("example-activation-key");

        assertHidden("UserRegistration.toString()", registration.toString(), MockHttp.API_KEY);
        assertHidden("UserRegistration.toString()", registration.toString(), PASSWORD);
        assertHidden("UserRegistration.toString()", registration.toString(), "example-activation-key");
    }

    @Test
    public void teamMemberRequestToStringHidesThePassword() {
        TeamMemberRequest request = new TeamMemberRequest();
        request.setMemberEmail("driver@example.com");
        request.setMemberPassword(PASSWORD);

        assertHidden("TeamMemberRequest.toString()", request.toString(), PASSWORD);
    }

    @Test
    public void telematicsMemberRegisterToStringHidesTheToken() {
        TelematicsMemberRegister registration = new TelematicsMemberRegister();
        registration.setTelematicRegisterID("REG-1");
        registration.setApiToken(PASSWORD);

        assertHidden("TelematicsMemberRegister.toString()", registration.toString(), PASSWORD);
    }

    @Test
    public void locationItemToStringHidesTheConnectionToken() {
        LocationCombinedDataItem item = new LocationCombinedDataItem();
        item.setAddress1("100 Example St");
        item.setExternalConnectionToken(PASSWORD);

        assertHidden("LocationCombinedDataItem.toString()", item.toString(), PASSWORD);
    }

    @Test
    public void memberResponseToStringHidesTheKeyAndTheSession() {
        MemberResponse member = new MemberResponse();
        member.setMemberId(42L);
        member.setApiKey(MockHttp.API_KEY);
        member.setSessionGuid("example-session-guid");
        member.setSessionId(987654321987L);

        assertHidden("MemberResponse.toString()", member.toString(), MockHttp.API_KEY);
        assertHidden("MemberResponse.toString()", member.toString(), "example-session-guid");
        assertHidden("MemberResponse.toString()", member.toString(), "987654321987");
        assertTrue("other fields are still shown", member.toString().contains("42"));
    }

    @Test
    public void userLicenseRequestToStringHidesTheSessionAndTheReceipt() {
        UserLicenseRequest request = new UserLicenseRequest();
        request.setSessionGuid("example-session-guid");
        request.setToken("example-purchase-token");
        request.setPayload("example-receipt");

        assertHidden("UserLicenseRequest.toString()", request.toString(), "example-session-guid");
        assertHidden("UserLicenseRequest.toString()", request.toString(), "example-purchase-token");
        assertHidden("UserLicenseRequest.toString()", request.toString(), "example-receipt");
    }

    @Test
    public void configurationValuesAreLeftOutOfToString() {
        ConfigurationData data = new ConfigurationData();
        data.setConfigKey("webhook_secret");
        data.setConfigValue(PASSWORD);
        MemberConfigurationSetting setting = new MemberConfigurationSetting();
        setting.setConfigKey("webhook_secret");
        setting.setConfigValue(PASSWORD);

        assertHidden("ConfigurationData.toString()", data.toString(), PASSWORD);
        assertHidden("MemberConfigurationSetting.toString()", setting.toString(), PASSWORD);
        assertTrue("the key is still shown", setting.toString().contains("webhook_secret"));
    }

    // ------------------------------------------------------------------ every model

    /**
     * A Java or wire field name that holds a credential: an API key, a password, a token, a
     * secret, a session, a stored configuration value, an activation key, a receipt.
     */
    private static final Pattern CREDENTIAL_NAME = Pattern.compile(
            "(?i).*(api_?key|password|passwd|token|secret|session_?(guid|id)|config_?value|activation_?key"
                    + "|credential|payload).*");

    /** Fields whose name looks like a credential but that hold none, with the reason. */
    private static final Map<String, String> NOT_CREDENTIALS = new HashMap<>();

    static {
        NOT_CREDENTIALS.put(TelematicsConnection.class.getName() + ".validateRemoteCredentials",
                "a flag: whether to check the credentials with the vendor");
        NOT_CREDENTIALS.put(TelematicsVendor.class.getName() + ".credentials",
                "the names of the credential fields a vendor needs, not their values");
        NOT_CREDENTIALS.put(TelematicsVendorV5.class.getName() + ".credentials",
                "the names of the credential fields a vendor needs, not their values");
    }

    /** The classes the sweep must find, so that a broken scan cannot pass by finding nothing. */
    private static final List<Class<?>> KNOWN_CREDENTIAL_CLASSES = Arrays.<Class<?>>asList(
            User.class, AccountProfile.class, TelematicsConnection.class, Registration.class,
            UserRegistration.class, TeamMemberRequest.class, TelematicsMemberRegister.class,
            LocationCombinedDataItem.class, MemberResponse.class, UserLicenseRequest.class,
            ConfigurationData.class, MemberConfigurationSetting.class);

    @Test
    public void noModelPrintsACredentialInToString() throws Exception {
        List<String> unloadable = new ArrayList<>();
        Map<Class<?>, List<Field>> models = credentialFields(unloadable);
        // A class the sweep cannot load would go unchecked, and a credential model behind it with it.
        assertEquals("classes the sweep could not load", Collections.emptyList(), unloadable);
        List<String> problems = new ArrayList<>();
        for (Map.Entry<Class<?>, List<Field>> model : models.entrySet()) {
            problems.addAll(leaks(model.getKey(), model.getValue()));
        }

        assertTrue("the sweep finds the known credential classes: " + models.keySet(),
                models.keySet().containsAll(KNOWN_CREDENTIAL_CLASSES));
        assertEquals("Exclude these fields from toString() with @ToString.Exclude, or add them to NOT_CREDENTIALS"
                + " with the reason", Collections.emptyList(), problems);
    }

    /**
     * Finds every concrete class of the SDK (main classes only) that has its own
     * {@code toString()} and a field whose Java name, {@code @SerializedName} or
     * {@code @QueryParameter} looks like a credential, inherited fields included. A class that
     * keeps {@code Object.toString()}, such as a manager, prints no field.
     *
     * @param unloadable where to list the classes that cannot be loaded, with the reason, so that
     *                   the test fails on them instead of skipping them
     */
    private static Map<Class<?>, List<Field>> credentialFields(List<String> unloadable)
            throws IOException, URISyntaxException, NoSuchMethodException {
        Path root = Paths.get(User.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        List<String> names;
        try (Stream<Path> files = Files.walk(root)) {
            names = files.map(root::relativize).map(Path::toString)
                    .filter(name -> name.endsWith(".class") && !name.endsWith("package-info.class"))
                    .map(name -> name.substring(0, name.length() - ".class".length()).replace('/', '.')
                            .replace('\\', '.'))
                    .collect(Collectors.toList());
        }
        Map<Class<?>, List<Field>> models = new LinkedHashMap<>();
        for (String name : new TreeSet<>(names)) {
            Class<?> type;
            try {
                type = Class.forName(name, false, User.class.getClassLoader());
            } catch (ClassNotFoundException | LinkageError ex) {
                unloadable.add(name + ": " + ex);
                continue;
            }
            if (type.isInterface() || type.isEnum() || type.isAnnotation() || type.isAnonymousClass()
                    || Modifier.isAbstract(type.getModifiers()) || type.isSynthetic()
                    || type.getMethod("toString").getDeclaringClass() == Object.class) {
                continue;
            }
            List<Field> fields = new ArrayList<>();
            for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers()) && !field.isSynthetic() && looksLikeACredential(field)
                            && !NOT_CREDENTIALS.containsKey(c.getName() + "." + field.getName())) {
                        fields.add(field);
                    }
                }
            }
            if (!fields.isEmpty()) {
                models.put(type, fields);
            }
        }
        return models;
    }

    /** Returns whether the Java name or a wire name of a field looks like a credential. */
    private static boolean looksLikeACredential(Field field) {
        SerializedName wire = field.getAnnotation(SerializedName.class);
        QueryParameter query = field.getAnnotation(QueryParameter.class);
        return CREDENTIAL_NAME.matcher(field.getName()).matches()
                || wire != null && CREDENTIAL_NAME.matcher(wire.value()).matches()
                || query != null && CREDENTIAL_NAME.matcher(query.value()).matches();
    }

    /**
     * Sets a distinct value into each credential field of a new instance and returns one
     * problem per value that {@code toString()} prints. The messages name the field, never the
     * value.
     */
    private static List<String> leaks(Class<?> type, List<Field> fields) throws ReflectiveOperationException {
        Constructor<?> constructor;
        try {
            constructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            return Collections.singletonList(type.getName() + " has no no-argument constructor, so the sweep cannot"
                    + " check its fields " + fields + "; add a case to this test");
        }
        constructor.setAccessible(true);
        Object model = constructor.newInstance();
        Map<Field, String> values = new LinkedHashMap<>();
        List<String> problems = new ArrayList<>();
        long number = 900_000_000_001L;
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            String text;
            if (fieldType == String.class) {
                text = "credential-value-" + values.size() + "-" + field.getName();
                field.set(model, text);
            } else if (fieldType == Long.class || fieldType == long.class) {
                text = String.valueOf(number++);
                field.set(model, Long.valueOf(text));
            } else {
                problems.add(type.getName() + "." + field.getName() + " has the type " + fieldType.getName()
                        + ", which the sweep cannot fill; add a case to this test");
                continue;
            }
            values.put(field, text);
        }
        String printed = model.toString();
        for (Map.Entry<Field, String> value : values.entrySet()) {
            if (printed.contains(value.getValue())) {
                problems.add(type.getName() + "." + value.getKey().getName() + " is printed by toString()");
            }
        }
        return problems;
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
