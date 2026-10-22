# Security policy

## Reporting a vulnerability

Please do not open a public GitHub issue for a security problem.

Email **support@route4me.com** with "Security: route4me-java-sdk" in the subject. Include the affected version and the steps to reproduce. We will acknowledge your report and keep you informed until a fix is released.

If you have committed a Route4Me API key anywhere, even in a private repository, rotate it in your Route4Me account at once. Removing it from git history does not invalidate it.

## Supported versions

| Version | Supported |
|---|---|
| 2.0.x | Yes |
| 1.16.x and older | No. Upgrade to the latest release |

The SDK supports Java 8 and newer. Every change is built and tested on Java 8, 11, 17 and 21.

## How the SDK protects your API key

- **The key goes to the Route4Me API hosts only.** The SDK sends the API key only to `api.route4me.com` and `wh.route4me.com`. A request to any other host carries no key at all. The deprecated `TelematicsManager` methods of the retired `telematics.route4me.com` host throw an `APIException` without sending a request.
- **HTTPS to Route4Me hosts only.** Every request goes over HTTPS. Redirects are followed only when they lead, over HTTPS, to `api.route4me.com` or `wh.route4me.com`; any other redirect, also to another Route4Me host, fails the request, because a redirect carries the key along (in the query, or in the `Authorization` header, which Apache HttpClient copies). The unit tests fail if a request goes anywhere else, over plain HTTP or to another port, or carries the key to any other host.
- **Bearer token for V5.** Requests to the V5 API (`https://wh.route4me.com/modules/api/v5.0/` and `v5.1/`) send the key in an `Authorization: Bearer` header, so it stays out of URLs and server logs. The V4 API requires the `api_key` query parameter, so V4 requests still use it. The SDK never puts the key in a request body.
- **Redaction.** `APIException` messages, URLs and response bodies have the key removed; response bodies also hide credential fields such as `password`, `connection_token` or `session_guid`, and an I/O error whose message quotes a URL with a credential is kept only as a redacted copy. The message never contains the raw response body or the request parameters. Model classes leave API keys, passwords and tokens out of `toString()`. Logged values have their line breaks removed and are cut to a safe length.
- **Logging.** The SDK never logs request headers or the API key. Apache HttpClient, which the SDK uses, does: at DEBUG level its `org.apache.http.headers` and `org.apache.http.wire` loggers print every request line and header, so the `Authorization: Bearer` header and the `api_key` query parameter. Do not enable DEBUG logging for `org.apache.http` in production, and treat such logs as secrets.
- **Path encoding.** IDs placed in a URL path are percent-encoded as a single segment, so a crafted ID such as `../secret` cannot send the request, and your key, to another endpoint.
- **Timeouts.** Every request has a connect timeout (6050 ms), a read timeout (30000 ms) and a timeout for getting a pooled connection (6050 ms), so a stalled server cannot hang your application. You can change them with the `setConnectTimeout`, `setSocketTimeout` and `setConnectionRequestTimeout` methods of each manager.

## Dependencies

The project checks its dependencies with `osv-scanner` and its history with `gitleaks`.

The WebSocket geocoder uses `socket.io-client` 1.0.2, because the Route4Me geocoding server runs Socket.IO 2, which the 2.x Java client cannot connect to. That release asks for `okhttp` 3.12.12 (with `okio` 1.15.0) and `org.json` 20090211, which have known vulnerabilities. The SDK declares the patched `okhttp` 4.12.0 (with `okio` 3.6.0, and the Kotlin standard library it needs) and `org.json` 20260814 as direct dependencies, so Maven (nearest wins) and Gradle (highest wins) resolve them for your project instead of the versions `socket.io-client` asks for; you do not need to add anything. Advisory GHSA-85xx-xhjm-rhrw (CVE-2022-25867) still lists `socket.io-client` 1.0.2, but that release contains the fix; `osv-scanner.toml` records why it is ignored.

If your build forces other versions of these libraries, for example through a BOM or `dependencyManagement`, keep them at these versions or later.
