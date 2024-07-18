package org.openmrs.maven.plugins.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ContentPropertiesValidatorTest {

    @Before
    public void setUp() {
        // Initialization if necessary
    }

    @Test
    public void testValidVersions() {
        assertTrue(ContentPropertiesValidator.isValidVersion("0.13.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("3.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("1.0.0"));
    }

    @Test
    public void testValidVersionRanges() {
        assertTrue(ContentPropertiesValidator.isValidVersion("^0.13.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("~0.13.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion(">0.13.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("<3.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion(">=3.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("<=3.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("=3.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("1.0.0 - 1.10.10"));
        assertTrue(ContentPropertiesValidator.isValidVersion("<2.1.0 || >2.6.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("=3.0.0"));
    }

    @Test
    public void testInvalidVersions() {
        assertFalse(ContentPropertiesValidator.isValidVersion("abc"));
        assertFalse(ContentPropertiesValidator.isValidVersion("1..0"));
        assertFalse(ContentPropertiesValidator.isValidVersion("1.0.0.0"));
        assertFalse(ContentPropertiesValidator.isValidVersion("latest")); // Ensure "latest" is not valid
    }

    @Test
    public void testPreReleaseAndBuildMetadata() {
        assertTrue(ContentPropertiesValidator.isValidVersion("1.0.0-alpha"));
        assertTrue(ContentPropertiesValidator.isValidVersion("1.0.0+20130313144700"));
        assertTrue(ContentPropertiesValidator.isValidVersion("1.0.0-beta+exp.sha.5114f85"));
    }

    @Test
    public void testComplexRanges() {
        assertFalse(ContentPropertiesValidator.isValidVersion(">=1.0.0 <2.0.0"));
        assertTrue(ContentPropertiesValidator.isValidVersion("1.2.3 - 2.3.4"));
        assertFalse(ContentPropertiesValidator.isValidVersion(">=1.2.3 <2.3.4 || >=3.0.0"));
    }
}
