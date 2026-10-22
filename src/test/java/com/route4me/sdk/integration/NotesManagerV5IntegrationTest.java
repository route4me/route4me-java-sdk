package com.route4me.sdk.integration;

import com.route4me.sdk.services.notes.*;
import com.route4me.sdk.exception.APIException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NotesManagerV5IntegrationTest extends BaseIntegrationTest {
    private NotesManagerV5 manager;

    @Before
    public void setUp() {
        super.checkApiKey();
        manager = new NotesManagerV5(API_KEY);
    }

    @Test
    public void testGetNoteCustomTypes() throws APIException {
        NoteCustomTypeCollection response = manager.getNoteCustomTypes();
        assertNotNull(response);
    }
}
