package com.route4me.sdk.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link Sanitizer}.
 */
public class SanitizerTest {

    private static final String KEY = "11111111111111111111111111111111";

    @Test
    public void redactUrlHidesCredentialParameters() {
        assertEquals("https://api.route4me.com/api.v4/route.php?route_id=R1&api_key=REDACTED",
                Sanitizer.redactUrl("https://api.route4me.com/api.v4/route.php?route_id=R1&api_key=" + KEY));
        assertEquals("https://x/?API_KEY=REDACTED&api_token=REDACTED&member_password=REDACTED&strPassword_1=REDACTED#f",
                Sanitizer.redactUrl("https://x/?API_KEY=a&api_token=b&member_password=c&strPassword_1=d#f"));
        assertEquals("https://x/?token=REDACTED&tokens=kept&token_type=kept&api_keys=kept",
                Sanitizer.redactUrl("https://x/?token=t&tokens=kept&token_type=kept&api_keys=kept"));
        assertEquals("https://x/?api_key[]=REDACTED", Sanitizer.redactUrl("https://x/?api_key[]=a"));
        assertNull(Sanitizer.redactUrl(null));
    }

    @Test
    public void redactUrlHidesParametersWhoseNameEndsLikeACredential() {
        assertEquals("https://wh.route4me.com/modules/api/v5.0/x?organization_api_key=REDACTED&page=2",
                Sanitizer.redactUrl("https://wh.route4me.com/modules/api/v5.0/x?organization_api_key=" + KEY
                        + "&page=2"));
        assertEquals("https://x/?ORGANIZATION_API_KEY=REDACTED&my_apikey=REDACTED&device_token=REDACTED"
                        + "&Client_Secret=REDACTED&new_password=REDACTED&NewPassword=REDACTED",
                Sanitizer.redactUrl("https://x/?ORGANIZATION_API_KEY=a&my_apikey=b&device_token=c"
                        + "&Client_Secret=d&new_password=e&NewPassword=f"));
        assertEquals("https://x/?route_id=R1&my_api_key=REDACTED#frag",
                Sanitizer.redactUrl("https://x/?route_id=R1&my_api_key=k#frag"));
    }

    @Test
    public void redactUrlHidesBracketedAndEncodedCredentialNames() {
        assertEquals("https://x/?organization_api_key[]=REDACTED&organization_api_key%5B%5D=REDACTED",
                Sanitizer.redactUrl("https://x/?organization_api_key[]=a&organization_api_key%5B%5D=b"));
        assertEquals("https://x/?filters[organization_api_key]=REDACTED&filters%5Bdevice_token%5D=REDACTED"
                        + "&filters[status]=1",
                Sanitizer.redactUrl("https://x/?filters[organization_api_key]=a&filters%5Bdevice_token%5D=b"
                        + "&filters[status]=1"));
        assertEquals("https://x/?organization%5Fapi%5Fkey=REDACTED",
                Sanitizer.redactUrl("https://x/?organization%5Fapi%5Fkey=a"));
        assertEquals("a malformed escape keeps the raw name", "https://x/?bad%zz_token=REDACTED",
                Sanitizer.redactUrl("https://x/?bad%zz_token=a"));
    }

    @Test
    public void redactUrlFindsACredentialInsideAnotherValue() {
        assertEquals("https://x/?next=https://y/?organization_api_key=REDACTED&b=2",
                Sanitizer.redactUrl("https://x/?next=https://y/?organization_api_key=k&b=2"));
    }

    @Test
    public void forLogHidesCredentialNamesToo() {
        assertEquals("GET https://x/?organization_api_key=REDACTED",
                Sanitizer.forLog("GET https://x/?organization_api_key=" + KEY));
    }

    @Test
    public void redactHidesTheSecretAsTypedAndEncoded() {
        assertEquals("key REDACTED and REDACTED", Sanitizer.redact("key " + KEY + " and " + KEY, KEY));
        assertEquals("REDACTED", Sanitizer.redact("a%2Bb", "a+b"));
        assertEquals("text", Sanitizer.redact("text", null));
        assertEquals("text", Sanitizer.redact("text", ""));
        assertNull(Sanitizer.redact(null, KEY));
    }

    @Test
    public void truncateMarksTheCut() {
        assertEquals("abcdef", Sanitizer.truncate("abcdef", 6));
        assertEquals("abc...", Sanitizer.truncate("abcdefg", 6));
        assertEquals("...", Sanitizer.truncate("abcdefg", 2));
        assertNull(Sanitizer.truncate(null, 5));
    }

    @Test
    public void forLogRemovesLineBreaksAndControlCharacters() {
        assertEquals("one two  three four five", Sanitizer.forLog("one\ntwo\r\nthree\tfour\u2028five"));
        assertEquals("null", Sanitizer.forLog(null));
        assertEquals("https://x/?api_key=REDACTED", Sanitizer.forLog("https://x/?api_key=" + KEY));
    }

    @Test
    public void forLogCutsLongValues() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            text.append("0123456789");
        }
        assertEquals(Sanitizer.DEFAULT_LOG_LENGTH, Sanitizer.forLog(text).length());
        assertEquals("01234...", Sanitizer.forLog(text, 8));
    }
}
