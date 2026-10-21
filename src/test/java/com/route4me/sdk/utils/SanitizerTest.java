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
        assertEquals("https://x/?token=REDACTED&tokens=kept&my_api_key=kept",
                Sanitizer.redactUrl("https://x/?token=t&tokens=kept&my_api_key=kept"));
        assertEquals("https://x/?api_key[]=REDACTED", Sanitizer.redactUrl("https://x/?api_key[]=a"));
        assertNull(Sanitizer.redactUrl(null));
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
