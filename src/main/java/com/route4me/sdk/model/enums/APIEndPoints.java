package com.route4me.sdk.model.enums;

/**
 *
 * @author juan
 */
public class APIEndPoints {

    public static final String DRIVER_VERSION = "route4me-java-driver-0.0.1";
    public static final String MAIN_HOST = "https://www.route4me.com";
    public static final String API_HOST = MAIN_HOST + "/api.v4/optimization_problem.php";
    public static final String SHOW_ROUTE_HOST = MAIN_HOST + "/route4me.php";
    public static final String ROUTE_HOST = MAIN_HOST + "/api.v4/route.php";
    public static final String SET_GPS_HOST = MAIN_HOST + "/track/set.php";
    public static final String BATCH_GEOCODER = MAIN_HOST + "/api/geocoder.php";
    public static final String SINGLE_GEOCODER = MAIN_HOST + "/api/address.php";
    public static final String EXPORTER = MAIN_HOST + "/actions/route/export_current_route.php";
    public static final String ADDRESS_HOST = MAIN_HOST + "/api.v4/address.php";
    public static final String GET_USERS_HOST = MAIN_HOST + "/api/member/view_users.php";
    public static final String ADD_ROUTE_NOTES_HOST = MAIN_HOST + "/actions/addRouteNotes.php";
    public static final String GET_ACTIVITIES_HOST = MAIN_HOST + "/api/get_activities.php";
    public static final String DUPLICATE_ROUTE = MAIN_HOST + "/actions/duplicate_route.php";
    public static final String MOVE_ROUTE_DESTINATION = MAIN_HOST + "/actions/route/move_route_destination.php";
    public static final String ADDRESSBOOK = MAIN_HOST + "/api.v4/address_book.php";
    public static final String AVOIDANCE = MAIN_HOST + "/api.v4/avoidance.php";

}
