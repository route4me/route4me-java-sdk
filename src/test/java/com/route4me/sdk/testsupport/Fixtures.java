package com.route4me.sdk.testsupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Reads canned response bodies from {@code src/test/resources/fixtures/}.
 *
 * <p>Fixtures hold fake data only: made-up IDs, example addresses, and never an API key.
 */
public final class Fixtures {

    private static final String FOLDER = "/fixtures/";

    /** Not instantiable: static helpers only. */
    private Fixtures() {
    }

    /**
     * Reads a fixture as UTF-8 text.
     *
     * @param name the file name inside {@code fixtures/}, such as {@code optimization.json}
     * @return the file content
     * @throws IllegalArgumentException if the fixture does not exist
     */
    public static String read(String name) {
        String resource = FOLDER + name;
        try (InputStream in = Fixtures.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("Fixture not found on the test classpath: " + resource);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read fixture " + resource, e);
        }
    }
}
