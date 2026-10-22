package com.route4me.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.route4me.sdk.queryconverter.QueryConverter;
import com.route4me.sdk.services.routing.Address;
import com.route4me.sdk.services.routing.BundledItem;
import com.route4me.sdk.services.routing.Route;
import com.route4me.sdk.services.routing.OptimizationParameters;
import com.route4me.sdk.services.routing.OrderInventory;
import com.route4me.sdk.services.routing.Parameters;
import com.route4me.sdk.services.orders.OrderV5;
import com.route4me.sdk.services.vehicles.VehicleV5;
import com.route4me.sdk.services.customers.CustomerStoreRequest;
import com.route4me.sdk.services.facilities.FacilityResource;
import com.route4me.sdk.services.facilities.FacilityCoordinates;
import com.route4me.sdk.services.schedules.Schedule;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ModelSerializationTest {

    private Gson gson;

    @Before
    public void setUp() {
        gson = new GsonBuilder().create();
    }

    @Test
    public void testAddressRoundTrip() {
        Address address = new Address();
        address.setAddress("123 Main St");
        address.setLatitude(40.7128);
        address.setLongitude(-74.0060);
        address.setAlias("HQ");
        address.setDepot(true);
        address.setTime(300L);

        String json = gson.toJson(address);
        Address deserialized = gson.fromJson(json, Address.class);

        assertEquals("123 Main St", deserialized.getAddress());
        assertEquals(40.7128, deserialized.getLatitude(), 0.0001);
        assertEquals(-74.0060, deserialized.getLongitude(), 0.0001);
        assertEquals("HQ", deserialized.getAlias());
        assertTrue(deserialized.getDepot());
        assertEquals(Long.valueOf(300L), deserialized.getTime());
    }

    @Test
    public void testAddressSerializedNameAnnotations() {
        Address address = new Address();
        address.setRouteDestinationId(12345L);
        address.setLatitude(40.0);
        address.setLongitude(-74.0);

        String json = gson.toJson(address);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("route_destination_id"));
        assertTrue(obj.has("lat"));
        assertTrue(obj.has("lng"));
    }

    @Test
    public void testParametersRoundTrip() {
        Parameters params = new Parameters();
        params.setRouteName("Test Route");
        params.setDistanceUnit("mi");
        params.setTravelMode("Driving");
        params.setOptimize("Distance");

        String json = gson.toJson(params);
        Parameters deserialized = gson.fromJson(json, Parameters.class);

        assertEquals("Test Route", deserialized.getRouteName());
        assertEquals("mi", deserialized.getDistanceUnit());
        assertEquals("Driving", deserialized.getTravelMode());
        assertEquals("Distance", deserialized.getOptimize());
    }

    @Test
    public void testParametersSerializedNameAnnotations() {
        Parameters params = new Parameters();
        params.setRouteName("Test");
        params.setDistanceUnit("mi");

        String json = gson.toJson(params);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("route_name"));
        assertTrue(obj.has("distance_unit"));
    }

    @Test
    public void testOptimizationParametersRoundTrip() {
        OptimizationParameters op = new OptimizationParameters();
        Parameters params = new Parameters();
        params.setRouteName("Opt Test");
        op.setParameters(params);

        Address addr = new Address();
        addr.setAddress("456 Oak Ave");
        addr.setLatitude(41.0);
        addr.setLongitude(-73.0);
        op.setAddresses(Collections.singletonList(addr));

        String json = gson.toJson(op);
        OptimizationParameters deserialized = gson.fromJson(json, OptimizationParameters.class);

        assertEquals("Opt Test", deserialized.getParameters().getRouteName());
        assertEquals(1, deserialized.getAddresses().size());
        assertEquals("456 Oak Ave", deserialized.getAddresses().get(0).getAddress());
    }

    @Test
    public void testOrderV5RoundTrip() {
        OrderV5 order = new OrderV5();
        order.setAddress1("789 Pine St");
        order.setAddressAlias("Customer A");
        order.setFirstName("John");
        order.setLastName("Doe");

        String json = gson.toJson(order);
        OrderV5 deserialized = gson.fromJson(json, OrderV5.class);

        assertEquals("789 Pine St", deserialized.getAddress1());
        assertEquals("Customer A", deserialized.getAddressAlias());
        assertEquals("John", deserialized.getFirstName());
        assertEquals("Doe", deserialized.getLastName());
    }

    @Test
    public void testOrderV5SerializedNameAnnotations() {
        OrderV5 order = new OrderV5();
        order.setAddress1("test");
        order.setFirstName("Jane");

        String json = gson.toJson(order);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("address_1"));
        assertTrue(obj.has("first_name"));
    }

    @Test
    public void testVehicleV5RoundTrip() {
        VehicleV5 vehicle = new VehicleV5();
        vehicle.setVehicleAlias("Truck A");
        vehicle.setVehicleMake("Ford");
        vehicle.setVehicleModel("Transit");
        vehicle.setVehicleModelYear(2022);
        vehicle.setVehicleLicensePlate("ABC123");

        String json = gson.toJson(vehicle);
        VehicleV5 deserialized = gson.fromJson(json, VehicleV5.class);

        assertEquals("Truck A", deserialized.getVehicleAlias());
        assertEquals("Ford", deserialized.getVehicleMake());
        assertEquals("Transit", deserialized.getVehicleModel());
        assertEquals(Integer.valueOf(2022), deserialized.getVehicleModelYear());
        assertEquals("ABC123", deserialized.getVehicleLicensePlate());
    }

    @Test
    public void testVehicleV5SerializedNameAnnotations() {
        VehicleV5 vehicle = new VehicleV5();
        vehicle.setVehicleAlias("Test");
        vehicle.setVehicleLicensePlate("XYZ");

        String json = gson.toJson(vehicle);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("vehicle_alias"));
        assertTrue(obj.has("vehicle_license_plate"));
    }

    @Test
    public void testCustomerStoreRequestRoundTrip() {
        CustomerStoreRequest request = new CustomerStoreRequest();
        request.setName("Acme Corp");
        request.setExternalId("EXT-001");
        request.setCurrency("USD");
        request.setStatus(1);
        request.setFacilityIds(Arrays.asList("fac-1", "fac-2"));

        String json = gson.toJson(request);
        CustomerStoreRequest deserialized = gson.fromJson(json, CustomerStoreRequest.class);

        assertEquals("Acme Corp", deserialized.getName());
        assertEquals("EXT-001", deserialized.getExternalId());
        assertEquals("USD", deserialized.getCurrency());
        assertEquals(Integer.valueOf(1), deserialized.getStatus());
        assertEquals(2, deserialized.getFacilityIds().size());
    }

    @Test
    public void testCustomerStoreRequestSerializedNameAnnotations() {
        CustomerStoreRequest request = new CustomerStoreRequest();
        request.setExternalId("ext");
        request.setTaxId("tax");

        String json = gson.toJson(request);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("external_id"));
        assertTrue(obj.has("tax_id"));
    }

    @Test
    public void testFacilityResourceRoundTrip() {
        FacilityResource facility = new FacilityResource();
        facility.setFacilityId("fac-123");
        facility.setFacilityAlias("Warehouse A");
        facility.setAddress("100 Industrial Blvd");
        facility.setStatus(1);
        FacilityCoordinates coords = new FacilityCoordinates();
        coords.setLat(40.0);
        coords.setLng(-74.0);
        facility.setCoordinates(coords);

        String json = gson.toJson(facility);
        FacilityResource deserialized = gson.fromJson(json, FacilityResource.class);

        assertEquals("fac-123", deserialized.getFacilityId());
        assertEquals("Warehouse A", deserialized.getFacilityAlias());
        assertEquals("100 Industrial Blvd", deserialized.getAddress());
        assertEquals(Integer.valueOf(1), deserialized.getStatus());
        assertEquals(40.0, deserialized.getCoordinates().getLat(), 0.0001);
        assertEquals(-74.0, deserialized.getCoordinates().getLng(), 0.0001);
    }

    @Test
    public void testFacilityResourceSerializedNameAnnotations() {
        FacilityResource facility = new FacilityResource();
        facility.setFacilityId("test");
        facility.setFacilityAlias("test");

        String json = gson.toJson(facility);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("facility_id"));
        assertTrue(obj.has("facility_alias"));
    }

    @Test
    public void testScheduleRoundTrip() {
        Schedule schedule = new Schedule();
        schedule.setName("Weekly Deliveries");
        schedule.setAdvanceInterval(7);
        schedule.setAdvanceScheduleIntervalDays(1);
        schedule.setTimezone("America/New_York");
        schedule.setScheduleBlacklist(Arrays.asList("2023-12-25", "2024-01-01"));

        String json = gson.toJson(schedule);
        Schedule deserialized = gson.fromJson(json, Schedule.class);

        assertEquals("Weekly Deliveries", deserialized.getName());
        assertEquals(Integer.valueOf(7), deserialized.getAdvanceInterval());
        assertEquals(Integer.valueOf(1), deserialized.getAdvanceScheduleIntervalDays());
        assertEquals("America/New_York", deserialized.getTimezone());
        assertEquals(2, deserialized.getScheduleBlacklist().size());
    }

    @Test
    public void testScheduleSerializedNameAnnotations() {
        Schedule schedule = new Schedule();
        schedule.setScheduleUid("uid-123");
        schedule.setAdvanceInterval(7);

        String json = gson.toJson(schedule);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("schedule_uid"));
        assertTrue(obj.has("advance_interval"));
    }

    @Test
    public void testNullFieldsNotSerialized() {
        Address address = new Address();
        address.setAddress("Test");

        String json = gson.toJson(address);
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertTrue(obj.has("address"));
        assertFalse(obj.has("route_destination_id"));
    }

    @Test
    public void testAddressConstructorWithParams() {
        Address address = new Address("123 Main St", true, "Depot", 40.0, -74.0, 600L);
        assertEquals("123 Main St", address.getAddress());
        assertTrue(address.getDepot());
        assertEquals("Depot", address.getAlias());
        assertEquals(40.0, address.getLatitude(), 0.0001);
        assertEquals(-74.0, address.getLongitude(), 0.0001);
        assertEquals(Long.valueOf(600L), address.getTime());
    }

    @Test
    public void testRouteReadsTheMemberApprovalAndBundleFields() {
        String json = "{\"route_id\":\"R1\",\"user_route_rating\":4,\"member_first_name\":\"Alex\","
                + "\"member_last_name\":\"Driver\",\"channel_name\":\"ch-1\",\"member_picture\":\"pic.png\","
                + "\"member_tracking_subheadline\":\"On the way\",\"approved_for_execution\":true,"
                + "\"scheduled_start_timestamp\":1761040000,\"dispatched_timestamp\":1761043600,"
                + "\"approved_revisions_counter\":2,\"geofence_polygon_type\":\"circle\",\"geofence_polygon_size\":50,"
                + "\"original_route\":{\"route_id\":\"R0\",\"member_first_name\":\"Sam\"},"
                + "\"bundle_items\":[{\"cube\":1.5,\"revenue\":10,\"pieces\":3,\"weight\":2.5,\"cost\":1,"
                + "\"service_time\":120,\"time_window_start\":3600,\"time_window_end\":7200,"
                + "\"custom_data\":{\"k\":\"v\"},\"addresses_id\":[11,12]}],"
                + "\"facility_ids\":[\"F1\",\"F2\"]}";

        Route route = gson.fromJson(json, Route.class);

        assertEquals(Long.valueOf(4), route.getUserRouteRating());
        assertEquals("Alex", route.getMemberFirstName());
        assertEquals("Driver", route.getMemberLastName());
        assertEquals("ch-1", route.getChannelName());
        assertEquals("pic.png", route.getMemberPicture());
        assertEquals("On the way", route.getMemberTrackingSubheadline());
        assertEquals(Boolean.TRUE, route.getApprovedForExecution());
        assertEquals(Long.valueOf(1761040000L), route.getScheduledStartTimestamp());
        assertEquals(Long.valueOf(1761043600L), route.getDispatchedTimestamp());
        assertEquals(Long.valueOf(2), route.getApprovedRevisionsCounter());
        assertEquals("circle", route.getGeofencePolygonType());
        assertEquals(Long.valueOf(50), route.getGeofencePolygonSize());
        assertEquals("R0", route.getOriginalRoute().getId());
        assertEquals("Sam", route.getOriginalRoute().getMemberFirstName());
        BundledItem bundle = route.getBundleItems().get(0);
        assertEquals(1.5, bundle.getCube(), 0.0);
        assertEquals(10.0, bundle.getRevenue(), 0.0);
        assertEquals(Integer.valueOf(3), bundle.getPieces());
        assertEquals(2.5, bundle.getWeight(), 0.0);
        assertEquals(1.0, bundle.getCost(), 0.0);
        assertEquals(Integer.valueOf(120), bundle.getServiceTime());
        assertEquals(Integer.valueOf(3600), bundle.getTimeWindowStart());
        assertEquals(Integer.valueOf(7200), bundle.getTimeWindowEnd());
        assertEquals("v", ((Map<?, ?>) bundle.getCustomData()).get("k"));
        assertEquals(Arrays.asList(11L, 12L), bundle.getAddressesId());
        assertEquals(Arrays.asList("F1", "F2"), route.getFacilityIds());
    }

    @Test
    public void testRouteWritesTheNewFieldsWithTheirApiNames() {
        Route route = new Route();
        route.setMemberFirstName("Alex");
        route.setApprovedForExecution(false);
        route.setFacilityIds(Collections.singletonList("F1"));

        JsonObject obj = gson.toJsonTree(route).getAsJsonObject();

        assertEquals("Alex", obj.get("member_first_name").getAsString());
        assertFalse(obj.get("approved_for_execution").getAsBoolean());
        assertEquals("F1", obj.getAsJsonArray("facility_ids").get(0).getAsString());
        assertFalse("unset fields are not sent", obj.has("dispatched_timestamp"));
    }

    @Test
    public void testAddressRequiredSkillsAndOrderFieldsRoundTrip() {
        Address address = new Address("100 Example St", 40.1, -74.1);
        address.setRequiredSkills(Arrays.asList("forklift", "hazmat"));
        address.setAdditionalStatus("left_at_door");
        address.setCustomFieldsConfig(Collections.singletonList("color"));
        address.setCustomFieldsConfigStrJson("[\"color\"]");
        address.setCustomFieldsStrJson("{\"color\":\"red\"}");
        address.setRouteName("Morning run");

        JsonObject obj = gson.toJsonTree(address).getAsJsonObject();

        assertEquals("forklift", obj.getAsJsonArray("required_skills").get(0).getAsString());
        assertEquals("hazmat", obj.getAsJsonArray("required_skills").get(1).getAsString());
        assertEquals("left_at_door", obj.get("additional_status").getAsString());
        assertEquals("color", obj.getAsJsonArray("custom_fields_config").get(0).getAsString());
        assertEquals("[\"color\"]", obj.get("custom_fields_config_str_json").getAsString());
        assertEquals("{\"color\":\"red\"}", obj.get("custom_fields_str_json").getAsString());
        assertEquals("Morning run", obj.get("route_name").getAsString());
        assertFalse("unset fields are not sent", obj.has("bundle_count") || obj.has("order_inventory"));

        Address copy = gson.fromJson(gson.toJson(address), Address.class);
        assertEquals(Arrays.asList("forklift", "hazmat"), copy.getRequiredSkills());
        assertEquals("Morning run", copy.getRouteName());
    }

    @Test
    public void testAddressReadsBundlesAndOrderInventory() {
        String json = "{\"address\":\"100 Example St\",\"bundle_count\":2,"
                + "\"bundle_items\":[{\"pieces\":2,\"addresses_id\":[5001,5002]}],"
                + "\"order_inventory\":[{\"inventory_id\":7,\"order_id\":7001,\"name\":\"Box\",\"quantity\":3,"
                + "\"total_weight\":4.5,\"total_volume\":0.5,\"total_cost\":9.0,\"total_price\":12.0,"
                + "\"created_at\":\"2026-10-21\",\"updated_at\":\"2026-10-22\"}]}";

        Address address = gson.fromJson(json, Address.class);

        assertEquals(Integer.valueOf(2), address.getBundleCount());
        assertEquals(Arrays.asList(5001L, 5002L), address.getBundleItems().get(0).getAddressesId());
        OrderInventory line = address.getOrderInventory().get(0);
        assertEquals(Long.valueOf(7), line.getInventoryId());
        assertEquals(Long.valueOf(7001), line.getOrderId());
        assertEquals("Box", line.getName());
        assertEquals(Integer.valueOf(3), line.getQuantity());
        assertEquals(4.5, line.getTotalWeight(), 0.0);
        assertEquals(0.5, line.getTotalVolume(), 0.0);
        assertEquals(9.0, line.getTotalCost(), 0.0);
        assertEquals(12.0, line.getTotalPrice(), 0.0);
        assertEquals("2026-10-21", line.getCreatedAt());
        assertEquals("2026-10-22", line.getUpdatedAt());
    }

    @Test
    public void testUnsetFlagsAreNotSent() throws Exception {
        Route route = new Route();
        Parameters parameters = new Parameters();

        assertFalse(gson.toJsonTree(route).getAsJsonObject().has("is_unrouted"));
        assertFalse(gson.toJsonTree(parameters).getAsJsonObject().has("use_mixed_pickup_delivery_demands"));
        assertTrue(QueryConverter.convertObjectToParameters(parameters).isEmpty());
        assertFalse(route.isUnrouted());
        assertNull(route.getUnrouted());
        assertFalse(parameters.isUseMixedPickupDeliveryDemands());
        assertNull(parameters.getUseMixedPickupDeliveryDemands());
    }

    @Test
    public void testSetFlagsAreSentAndCanBeCleared() {
        Route route = new Route();
        Parameters parameters = new Parameters();

        route.setUnrouted(true);
        parameters.setUseMixedPickupDeliveryDemands(false);
        JsonObject routeJson = gson.toJsonTree(route).getAsJsonObject();
        JsonObject parametersJson = gson.toJsonTree(parameters).getAsJsonObject();

        assertTrue(routeJson.get("is_unrouted").getAsBoolean());
        assertFalse(parametersJson.get("use_mixed_pickup_delivery_demands").getAsBoolean());
        assertTrue(route.isUnrouted());
        assertEquals(Boolean.FALSE, parameters.getUseMixedPickupDeliveryDemands());

        parameters.isUseMixedPickupDeliveryDemands(true);
        assertTrue(parameters.isUseMixedPickupDeliveryDemands());
        route.setUnrouted((Boolean) null);
        parameters.setUseMixedPickupDeliveryDemands((Boolean) null);
        assertFalse(gson.toJsonTree(route).getAsJsonObject().has("is_unrouted"));
        assertFalse(gson.toJsonTree(parameters).getAsJsonObject().has("use_mixed_pickup_delivery_demands"));
        assertTrue(gson.fromJson("{\"is_unrouted\":true}", Route.class).isUnrouted());
    }
}
