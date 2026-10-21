package com.route4me.sdk.services;

import com.route4me.sdk.services.activities.ActivityType;
import com.route4me.sdk.services.geocoding.GeocoderOptions;
import com.route4me.sdk.services.notes.StatusUpdateType;
import com.route4me.sdk.services.orders.OrderStatus;
import com.route4me.sdk.services.routing.BundlingEnum;
import com.route4me.sdk.services.routing.Constants;
import com.route4me.sdk.services.routing.SourceType;
import com.route4me.sdk.services.routing.balance.BalanceMode;
import com.route4me.sdk.services.routing.breaks.RouteBreaksEnum;
import com.route4me.sdk.services.routing.dynamic.insert.InsertMode;
import com.route4me.sdk.services.routing.dynamic.insert.RecommendedBy;
import com.route4me.sdk.services.users.UsersEnum;
import com.route4me.sdk.services.vehicles.Vehicles;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Checks the SDK's value enums: the string each constant sends to the API.
 *
 * <p>Query parameters are built with {@code toString()} (see {@code QueryConverter}) while
 * request bodies often use {@code getValue()}, so the two must agree.
 */
public class EnumValuesTest {

    /** Every enum whose constants carry an API value. */
    private static List<Class<?>> valueEnums() {
        List<Class<?>> enums = new ArrayList<>();
        for (Class<?> nested : Constants.class.getDeclaredClasses()) {
            if (nested.isEnum()) {
                enums.add(nested);
            }
        }
        enums.addAll(Arrays.<Class<?>>asList(
                BundlingEnum.BundlingMode.class, BundlingEnum.BundlingFirstItemMode.class,
                BundlingEnum.BundlingAdditionalItemMode.class, BundlingEnum.BundledItemsMode.class,
                RouteBreaksEnum.RouteBreaksType.class, UsersEnum.Plan.class, InsertMode.class, RecommendedBy.class,
                BalanceMode.class, StatusUpdateType.class, ActivityType.class, SourceType.class,
                GeocoderOptions.GeocoderDetails.class, GeocoderOptions.GeocoderResponseFormat.class,
                Vehicles.VehicleMake.class));
        return enums;
    }

    @Test
    public void toStringAndGetValueAgree() throws Exception {
        List<Class<?>> enums = valueEnums();
        assertTrue("expected the Constants enums to be found", enums.size() > 30);
        for (Class<?> type : enums) {
            if (overridesToString(type)) {
                assertValuesMatchToString(type);
            }
        }
    }

    @Ignore("bug (unfiled): Constants.AlgorithmType, TypeofMatrix and DirectionsMethod toString() return the "
            + "constant name instead of the API value")
    @Test
    public void everyValueEnumReturnsItsApiValueFromToString() throws Exception {
        for (Class<?> type : valueEnums()) {
            assertValuesMatchToString(type);
        }
    }

    @Test
    public void numericCodesAreDistinctWithinEachEnum() throws Exception {
        int checked = 0;
        for (Class<?> type : valueEnums()) {
            Method getValue = findGetValue(type);
            if (getValue == null || getValue.getReturnType() != int.class) {
                continue;
            }
            Set<Object> codes = new HashSet<>();
            for (Object constant : type.getEnumConstants()) {
                assertTrue(type.getSimpleName() + " repeats code " + getValue.invoke(constant),
                        codes.add(getValue.invoke(constant)));
            }
            checked++;
        }
        assertTrue("expected the numeric enums to be found", checked >= 8);
    }

    @Test
    public void knownApiValues() {
        assertEquals("depots", SourceType.DEPOTS.getValue());
        assertEquals("input_addresses", SourceType.ADDRESSES.toString());
        assertEquals("certain_number_of_locations", RouteBreaksEnum.RouteBreaksType.CERTAIN_NUMBER_OF_LOCATIONS.getValue());
        assertEquals("optimal_anywhere", InsertMode.OPTIMAL_ANYWHERE.getValue());
        assertEquals("user_message", ActivityType.USER_MESSAGE.toString());
        assertEquals("dropoff", StatusUpdateType.DROPOFF.toString());
    }

    @Test
    public void numericCodesRoundTripThroughGet() throws Exception {
        List<Class<?>> enums = valueEnums();
        enums.add(OrderStatus.class);
        int checked = 0;
        for (Class<?> type : enums) {
            Method getValue = findGetValue(type);
            Method lookup = findStaticGet(type);
            if (getValue == null || getValue.getReturnType() != int.class || lookup == null) {
                continue;
            }
            for (Object constant : type.getEnumConstants()) {
                assertSame(type.getSimpleName() + "." + constant, constant, lookup.invoke(null, getValue.invoke(constant)));
            }
            checked++;
        }
        assertTrue("expected enums with a get(int) lookup", checked >= 5);
    }

    @Ignore("bug #40: SourceType is mutable; setValue changes the shared enum constant")
    @Test
    public void sourceTypeIsImmutable() throws Exception {
        Field value = SourceType.class.getDeclaredField("value");
        assertTrue("SourceType.value should be final", Modifier.isFinal(value.getModifiers()));
        for (Method method : SourceType.class.getMethods()) {
            assertFalse("SourceType should not have a public " + method.getName(), "setValue".equals(method.getName()));
        }
    }

    /** Asserts that each constant's {@code toString()} equals its String {@code getValue()}. */
    private static void assertValuesMatchToString(Class<?> type) throws Exception {
        Method getValue = findGetValue(type);
        for (Object constant : type.getEnumConstants()) {
            String name = type.getSimpleName() + "." + ((Enum<?>) constant).name();
            assertNotNull(name + " has no text", constant.toString());
            if (getValue != null && getValue.getReturnType() == String.class) {
                assertEquals(name, getValue.invoke(constant), constant.toString());
            }
        }
    }

    /** Returns whether the enum declares its own {@code toString()}. */
    private static boolean overridesToString(Class<?> type) {
        try {
            type.getDeclaredMethod("toString");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /** Returns the enum's static {@code get(int)} lookup, or {@code null}. */
    private static Method findStaticGet(Class<?> type) {
        try {
            Method lookup = type.getMethod("get", int.class);
            return Modifier.isStatic(lookup.getModifiers()) ? lookup : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /** Returns the enum's {@code getValue()} method, or {@code null}. */
    private static Method findGetValue(Class<?> type) {
        try {
            return type.getMethod("getValue");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
