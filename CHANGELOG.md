# CHANGELOG

This is the history of changes of the Route4Me Java SDK (`io.github.route4me:route4me-java-sdk`).

> Please follow this [guide](https://keepachangelog.com/en/1.0.0/).

## [2.0.0] - Unreleased

This is a major release: it removes and changes public API (see **Breaking changes**). The SDK still targets **Java 8**: the jar is built with `--release 8` and runs unchanged on Java 11, 17, 21 and later. CI tests it on JDK 8, 11, 17 and 21.

This release fixes every open bug report (#16–#19, #31–#41), adds 27 managers and 243 API methods for the operations the coverage issues list (#42–#104), and hardens how the SDK handles the API key. It also adds an offline test suite of 716 tests behind a request guard, and Javadoc for the whole public API. Several fixes change behaviour that code may rely on, so read **Breaking changes** before upgrading.

### Breaking changes

**Source and binary compatibility**
- **The SDK enums are immutable.** `setValue()` is removed from every enum, and their value fields are final (#40). No SDK code called the setters.
- `Constants.AlgorithmType`, `TypeofMatrix` and `DirectionsMethod`: `toString()` returns the API value (for example `"1"` instead of `"TSP"`), like the other `Constants` enums, and Gson writes that value. Use `name()` for the constant name.
- **The examples are no longer in the jar.** The 1.16.x jars shipped them as public classes in `com.route4me.sdk.examples` (and the 1.16.x pom brought OpenCSV for them). They now live in `src/examples/java`; see **Examples**.
- **`setGPS` returns the new `SetGpsResponse`** instead of `void` (#39). Callers compile unchanged but must be recompiled: the change is binary-incompatible.
- **The public static `GeocoderWebSockets.geocoderWebSocket` is removed.** Each `GeocoderWebSockets` now has its own socket, so two geocoders no longer share, and close, one connection.
- `OrderFilter` is now public, with chained setters. Its field `scheduled_for_YYMMDD` is renamed `scheduledForYYMMDD`, so the accessors change.
- `ThreadBasedGeocoding` implements `Callable<Address>` instead of the raw `Callable`, and its `call()` no longer declares `throws Exception`. Callers compile unchanged unless they catch a checked exception around `call()` or override it.
- `Route.isUnrouted` and `Parameters.useMixedPickupDeliveryDemands` start as `null` and are sent only when set. The old accessors keep their signatures and return `false` when unset; new accessors `getUnrouted()`/`setUnrouted(Boolean)` and `getUseMixedPickupDeliveryDemands()`/`setUseMixedPickupDeliveryDemands(Boolean)` expose the three states.

**Transport**
- **Requests time out.** Connect and connection-request timeouts are 6,050 ms and the read timeout is 30,000 ms (`Manager.DEFAULT_CONNECT_TIMEOUT_MS`, `DEFAULT_SOCKET_TIMEOUT_MS`, `DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS`, each with a getter and setter). Before, a stuck call hung forever.
- **`Manager` implements `Closeable`** and keeps one pooled HTTP client per instance. Reuse managers and `close()` them; a manager used after `close()` builds a new client. The pool grows to the thread count of a bulk geocode. A request still running when another thread calls `close()` fails with an `APIException` ("the manager was closed while the request ran"), not with the unchecked `IllegalStateException` of the closed pool.
- **API V5 requests send the key as `Authorization: Bearer <key>`** instead of the `api_key` query parameter, so the key no longer appears in URLs, proxy logs or server logs. V4 requests are unchanged.
- **API V5 requests send `Accept: application/json, */*;q=0.8`.** Without it, API V5 answered errors with an HTML page, so their messages were lost. File exports still work.
- **Redirects are followed only over https to the Route4Me API hosts**, `api.route4me.com` and `wh.route4me.com`. Any other redirect fails with an `APIException` caused by a `RedirectException` (new class `http.Route4MeRedirectStrategy`).
- **Query booleans are sent as `1` and `0`.** `false` was sent as the text `"false"`, which PHP reads as true; `getTerritory` sent its `orders` and `addresses` flags the same way.
- **API V5 list and map query values use bracketed keys**, for example `ids[]=1&ids[]=2` and `filters[status]=1` (`QueryConverter.toBracketedParameters`). PHP keeps only the last value of a repeated plain key.
- IDs in URL paths are percent-encoded. A `null`, empty, `.` or `..` ID throws `IllegalArgumentException` before any request.
- JSON bodies are sent as `application/json; charset=UTF-8`, form bodies as UTF-8 (they were ISO-8859-1), and requests without a body send no `Content-Type`. Responses are decoded with their declared charset, or UTF-8.
- The caller's `URIBuilder` is no longer modified. A response without a body (204) parses to `null`, or `""` for `String`.

**Errors**
- `APIException` carries the HTTP status (`getStatusCode`, 0 when there was no response), the body cut to 2,000 characters (`getResponseBody`), the parsed `errors`, V5 `error`, `code` and `messages` (`getErrors`, `getCode`, `getMessages`), the method and the URL with credentials redacted (`getMethod`, `getUrl`). `APIException(Exception)` keeps the cause (#19).
- Its message formats changed: "Invalid status code N for METHOD URL: first error"; "Unexpected server response for ...: the body is not valid JSON for X"; "METHOD URL failed: reason".
- The two `Manager.makeJSONRequest(method, builder, body, requestParams, ...)` overloads throw `APIException` when `requestParams` cannot be converted to query parameters, as the other two did; they logged the error and sent the request without its query.

**Manager methods**
- `updateTerritory` and `updateAvoidanceZone` send PUT with `territory_id` (#31). `getTerritory` leaves out `null` flags.
- `updateOptimization` puts `optimization_problem_id`, `reoptimize` and `show_directions` in the query; `reOptimizationByOptimizationID` sends no body.
- `deleteRoutes` joins the IDs without a trailing comma (#41); with no IDs, or a `null` or empty ID, it throws `IllegalArgumentException`.
- `getOrders` with a filter sends POST with a JSON body. `OrderRequest` fields and `DisplayType` constants serialise with their API names (#34).
- Geocoding (#32): `bulkGeocoder` returns one result per input, in input order, and no longer drops unmatched addresses. It enforces a deadline of `maxTimeout` per round of `maxThreads` addresses; the default `maxTimeout` goes from 500 to 30,000 ms (`GeocoderOptions.DEFAULT_MAX_TIMEOUT_MS`), since the old value never applied. `maxRetries` is the number of attempts, and 0 means one attempt.
- Null handling (#35, #36): `logActivity` throws `APIException` on an empty answer or a missing status; the `insertBreak*` methods throw `IllegalArgumentException` for a `null` amount or no route IDs; the `delete*` methods return `false` on an empty answer; several list getters return `null` on an empty body.
- `getContact` sends the plain ID (#33) and returns `null` when nothing matches.
- In classes of 1.16.1:
  - `Activity` reads `route_destination_id` (it mapped the typo `route_destination_od`), and `ActivityRequest` sends its type filter as `activity_type`, as C#, PHP and Node do (it sent `type`).
  - **`Order.getCustomUserFields()` returns `List<OrderCustomUserField>`** (it was typed with the package-private `OrderCustomUserFields`, which code outside the package could not use; that class is removed, and `OrderCustomFieldTypeInfo` is now public).
  - `RouteBreaksManager` rejects a `null` break duration, and `DynamicInsertManager` a `null` latitude or longitude, with `IllegalArgumentException` before sending; the API refused those requests anyway.
  - `RoutingManager.updateRoute(Route)`, and `assignDriver` and `assignVehicle`, which call it, reject a `null` route or a `null` or empty route ID with `IllegalArgumentException` before sending: `updateRoute` threw a `NullPointerException` for a `null` route, and sent a bare `route_id` for a missing ID.
  - `QueryConverter.convertObjectToParameters` leaves out `null` items of array and list fields; it threw a `NullPointerException`.
  - `RoutesRequest` gains `route_path_output` and `compress_path_points`; `Territory` gains `addresses_count` and `orders_count`; `TrackingHistory` gains its speed unit, member and source fields.
- **The V5 managers are new in this release** (they were never released before), so their request and reply classes follow the specs as they are now. Among the fixes made while covering the issues: vehicle, vehicle profile, capacity profile and team member updates use PATCH; `searchVehicles` posts its body; the address book list, batch update and export bodies, the order search, batch and archive bodies, and the customer `order_by` match the spec; barcode scans send `lng`; `schedule_blacklist` is a list of `{enabled, start, end}`; team bulk inserts go to `/team/bulk-insert` (the spec's `/team/users/bulk-insert` answers 405).
- **Booleans that the API sends as `1`/`0` are read correctly** (`LenientBooleanAdapter`, on every manager's JSON mapper). Gson rejected them, so a whole reply failed to parse, for example the vehicle profiles. Strings read as in 1.16.1: `"1"` and `"true"` (any case) are true, any other text, `""` included, is false, and a string never fails the parse.
- **`Map` fields read an empty JSON array as an empty map** (`LenientMapAdapterFactory`, on every manager's JSON mapper, in `AsyncJob.getBodyAs` and in `GeocoderWebSockets`). PHP sends an empty map as `[]`, for example `"EXT_FIELD_custom_data": []` on an order without custom data. `null` stays `null`, objects read as before, and a non-empty array now fails instead of being read as Gson's list of key and value pairs.

### Security
- **The API key goes only to `api.route4me.com` and `wh.route4me.com`.** V4 requests send it as the `api_key` query parameter, V5 requests (`/modules/api/v5.0` and `v5.1`) as a bearer token. A request to any other host carries no key.
- The 11 deprecated `TelematicsManager` methods of the retired `telematics.route4me.com` host throw an `APIException` without sending a request. In 1.16.1 they sent the API key to that host.
- Redirects are followed only to `api.route4me.com` and `wh.route4me.com`. `rapid.route4me.com` and `validator.route4me.com` are no longer allowed: no SDK call goes there over HTTP, and Apache HttpClient copies the `Authorization` header onto a redirect.
- The unit-test request guard fails any request that carries the API key to another host, deprecated endpoints included, or that uses a port other than 443.
- No credentials in `toString()`: `User`, `AccountProfile`, `TelematicsConnection`, `Registration`, `UserRegistration`, `TeamMemberRequest`, `TelematicsMemberRegister` and `LocationCombinedDataItem` no longer print API keys, passwords, session IDs, activation keys or connection tokens. A test checks every model.
- The response body that `APIException` keeps hides credential members by the same name rule (`"password"`, `"connection_token"`, `"session_guid"`, any name ending in `api_key`, and so on), not only the manager's own key. When the message of an I/O failure quotes a URL with a credential (Apache HttpClient quotes the whole target of a refused or circular redirect), the exception keeps a redacted copy of the cause chain instead.
- `SECURITY.md` warns that Apache HttpClient's own DEBUG logging (`org.apache.http.headers`, `org.apache.http.wire`) prints the `Authorization` header and the `api_key` parameter.
- `APIException` and the logs redact credential query parameters by name (`api_key`, and any name ending in `api_key`, `apikey`, `_token`, `_secret` or `password`, such as `organization_api_key`). New utility class `utils.Sanitizer`.
- Dependencies: opencsv is removed (it brought old `commons-beanutils`); `log4j-api` 2.25.5, `gson` 2.14.0. `okhttp` 4.12.0 (with `okio` 3.6.0 and the Kotlin standard library) and `org.json` 20260814 are declared as direct dependencies, so Maven and Gradle projects that use the SDK resolve them instead of the vulnerable `okhttp` 3.12.12, `okio` 1.15.0 and `org.json` 20090211 that `socket.io-client` 1.0.2 asks for. osv-scanner reports no known vulnerability; one advisory for socket.io-client is ignored until 2027-04-30, with the reason in `osv-scanner.toml` (2.x cannot connect to `validator.route4me.com`).
- New `SECURITY.md`: how to report a vulnerability and which versions get fixes.

### Added
- **API coverage (#42–#104).** 27 new managers and 243 new manager methods since 1.16.1, for the Route4Me API V4 and V5 operations the coverage issues list. Every request shape comes from the OpenAPI specs, then the C# SDK; the issues' own paths were often wrong. Each method has a unit test that pins its HTTP method, URL, query and body, and a runnable example.
  - New managers: `AccountProfileManager`, `AddressBarcodeManager`, `AddressBookManagerV5`, `AuthHistoryManager`, `AutocompleteManager`, `ConfigurationSettingsManager`, `ConfigurationSettingsManagerV5`, `CustomerManager`, `DistanceManager`, `DriverReviewManager`, `DriversManager`, `FacilityManager`, `FileUploadManager`, `LocationManager`, `NotesManagerV5`, `OptimizationProfileManager`, `OrderManagerV5`, `PodWorkflowManager`, `RouteCustomDataManager`, `RouteDestinationsManager`, `RouteStatusManager`, `RoutingManagerV5`, `SchedulesManager`, `TeamManagementManagerV5`, `TerritoriesManagerV5`, `TrackingHistoryManager` and `VehiclesManagerV5` (with vehicle and capacity profiles).
  - New methods on the 1.16.1 managers, for example `RoutingManager` (hybrid optimization, merge, share, mark departed and visited), `AddressBookManager` (groups, searches), `OrdersManager` (custom user fields, order updates), `TrackingManager` (device location history, user locations, batch pings, `findAsset`), `GeocodingManager` (batch geocoding), `UsersManager` (authentication, session validation) and `ActivityManager` (the activity feed).
  - Methods that start a background job return an `AsyncJob` with the job ID (from `X-Job-Id` or `Location`), so the job status and result calls can follow it. They are the address book update, delete and export by areas, `deleteContacts` and the exports, `LocationManager.exportLocations`, the five vehicle bulk calls, `TeamManagementManagerV5.bulkCreateTeamMembers` and `TerritoriesManagerV5.createAutomaticTerritoriesJob`. Subclasses can use `Manager.makeJobRequest` for the same.
- `TravelMode.BICYCLING` (#16), `RequestMethod.PATCH`.
- `Route`: the 15 fields C# has and Java lacked, such as `member_first_name`, `approved_for_execution` and `facility_ids`; `original_route` is typed as `Route`; new `BundledItem` (#17).
- `Address`: `required_skills` and 8 more fields from C#; new `OrderInventory` (#18).
- Telematics V5: `getTelematicsVendorsV5()`, `getTelematicsVendorsV5(TelematicsVendorsV5Request)`, `getTelematicsVendorV5(long)`.
- `forwardGeocode(builder, clazz, maxAttempts)`.

### Deprecated
- The 11 `TelematicsManager` methods on `telematics.route4me.com`, which is retired (it answers 301 to the marketing site), and `TELEMATICS_VENDORS_V4`. They throw an `APIException` without sending a request. Use the V5 vendor methods. Removal comes in the next major version.
- `RoutingManager.resequenceReoptimizeRoute` (`/api.v3/route/reoptimize_2.php`, `[Obsolete]` in C#): use `updateRoute(Route, RouteUpdateParameters)` with `reoptimize`. It is new in 2.0.0, added for C# parity and deprecated at once.
- Marked deprecated because the C# SDK marks the same calls `[Obsolete]`; they still work, and will be removed in the next major version:
  - `UsersManager` on `/api.v4/user.php`: `createUser` (use `TeamManagementManagerV5.createTeamMember`), `updateUser` (`updateTeamMember`), `getUser` (`getTeamMemberById`), `userDelete` (`deleteTeamMember`) and `getUsers()` (`getTeamMembers`). `getUsers(int, int)` (`view_users.php`), `userAuthentication`, `validateSession` and the other methods are not deprecated.
  - `AddressBookManager` on `/api.v4/address_book.php`: `getContacts` (use `AddressBookManagerV5.getContacts`), `createContact` (`addContact`), `updateContact` (`updateContact(long, AddressBookContactV5)`) and `deleteContact` (`deleteContacts`). `getContact`, `searchAddressBookLocation` and `searchAddressBookContactsByFilter` are not deprecated.
  - The whole `VehiclesManager` (the older `/modules/api/vehicles` API): `getVehicles`, `getVehicle`, `newVehicle`, `updateVehicle` and `deleteVehicle`. Use `VehiclesManagerV5`.
- Not added, with the reason in the issue comments: the rapid street-data service (404), the retired hosts `oa.route4me.com`, `telematics.route4me.com` and `apps.route4me.com/depots`, the obsolete route fallback endpoints (404), web-session calls, and endpoints that are not Route4Me's (Chamberlain MyQ, an OCR service over plain HTTP).

### Fixed
- #31 territory updates used DELETE. #32 busy-wait in `bulkGeocoder`. #33 quoted `address_id`. #34 `OrderRequest` field names. #35 unboxing NPE. #36 `DynamicInsertManager` NPE. #37 `return` in `finally`. #38 telematics searches delegated to the wrong method. #39 `setGPS` (it already used query parameters; it now returns the reply). #40 mutable enums. #41 trailing comma in `deleteRoutes`.
- `Constants.DeviceType` and `OptimizationParameters` serialise with their API names.
- `bulkGeocoder` keeps the latitude and longitude of a match that comes without `coordinates`; they were overwritten with 0.
- `RoutingManager.deleteRouteDestinations` sends the form field `route_destination_id` (the IDs, comma-separated), which the API reads; it answered `409` ("Unspecified required destination ID") to the `route_destination_ids` sent before.
- `NotesManager.addCustomNoteType` sends `type` and `values`, as the C# and PHP SDKs do, and requires at least one value; the API answered `400` ("Field values is required") to the spec's `note_custom_type` and `note_custom_type_values`.
- `GeocoderWebSockets` unsubscribes from `addresses_bulk` on close, and sizes its download chunks from the job size.

### Examples
- The examples moved from `src/main/java` to `src/examples/java` and compile with every build. They are no longer in the jar: the `com.route4me.sdk.examples` classes that the 1.16.x jars contained are gone. Each has a class comment saying what it shows and which method it calls, and reads the key from `R4M_API_KEY`. They call the live API, so run them against a test account. The examples that change or delete existing data (`DeleteRoute`, `DeleteRoutes`, `DeleteAddress`, the `MoveAddressesToRoute` pair, `UpdateRoute`, `UpdateRouteParameters`, `UpdateAddressBookContact`, `UpdateUser`, `DeleteUser`, `UpdateVehicle`, `DeleteVehicle` and `TheadBasedAddressesAliasUpdater`) take their targets from environment variables such as `ROUTE_ID` and stop when they are not set; they no longer pick the newest route or the first contact. Passwords and connection tokens come from environment variables too, and the contact data is fake (`example.com`, `555` numbers).
- 368 runnable examples, one for each new method, plus the `CsvRows` helper; the example issues (#105–#109) each get theirs. The examples of deprecated methods whose V5 replacement had no example now show the replacement (`DeleteUser`, `CreateAddressBookContact`, `UpdateAddressBookContact`, `GetVehicle`, `DeleteVehicle`); the others still show the deprecated method and name the V5 example to use.

### Internal
- 716 offline unit tests (there were none in the repo): every request is checked against an endpoint registry built from the Route4Me OpenAPI specs, with the host, the scheme and the key placement. JaCoCo enforces a coverage floor.
- The unit tests fail closed: a connection to any host but the loopback address fails the test (a default `ProxySelector` that the surefire listener and `MockHttp` install), so a manager a test forgot to mock, or the WebSocket geocoder, cannot reach the network. The key check also reads the SDK's Log4j 2 events at every level and its `java.util.logging` records down to `FINE`, `MockHttp` accepts only managers built with its placeholder key, and a guard failure is reported even when the test is then skipped.
- Checkstyle 9.3 with zero violations, `javac -Xlint:all -Werror`, and Javadoc doclint on every build. Every public type, method and field has Javadoc.
- CI: JDK 8, 11, 17 and 21; Actions pinned to commit SHAs; `permissions: contents: read`; a gitleaks job. The Travis config is removed.
