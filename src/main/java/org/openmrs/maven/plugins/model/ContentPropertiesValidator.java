package org.openmrs.maven.plugins.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * This class is used by Maven to perform validation of property values based on a semantic versioning format using regular expressions.
 * It validates whether the property values in a given properties file match the expected semantic versioning patterns.
 * The validation includes support for range operators and excludes specific non-version strings like "latest".
 * 
 */
public class ContentPropertiesValidator {

    // Enhanced regex pattern to include range operators and support pre-release and build metadata
    private static final Pattern VERSION_PATTERN = Pattern.compile(
        "^(\\s*[=><~^]*\\s*\\d+(\\.\\d+){0,2}(-[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?(\\+[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?)" +
        "(\\s*(-|\\|\\|)\\s*[=><~^]*\\s*\\d+(\\.\\d+){0,2}(-[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?(\\+[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?)*$"
    );

    /**
     * Validates the properties in the given file.
     *
     * @param filePath the path to the properties file
     * @return true if all property values are valid version formats, false otherwise
     * @throws IOException if an error occurs while reading the file
     */
    public static boolean validateProperties(String filePath) throws IOException {
        try (InputStream input = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(input);

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                if (!isValidVersion(value)) {
                    System.out.println("Invalid version format for key: " + key + ", value: " + value);
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cleans the value by trimming whitespace.
     *
     * @param value the value to clean
     * @return the cleaned value
     */
    private static String cleanValue(String value) {
        return value.trim();
    }

    /**
     * Checks if the given value is a valid version format.
     *
     * @param value the value to check
     * @return true if the value is a valid version format, false otherwise
     */
    protected static boolean isValidVersion(String value) {
        value = cleanValue(value);
        // Exclude "latest" explicitly
        if ("latest".equalsIgnoreCase(value)) {
            return false;
        }
        return VERSION_PATTERN.matcher(value).matches();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java org.openmrs.maven.plugins.model.ContentPropertiesValidator <properties-file-path>");
            System.exit(1);
        }

        String filePath = args[0];
        try {
            boolean isValid = validateProperties(filePath);
            if (isValid) {
                System.out.println("All properties have valid version formats.");
                System.exit(0);  // Exit with zero status to indicate success
            } else {
                System.out.println("Some properties have invalid version formats.");
                System.exit(1);  // Exit with non-zero status to indicate failure
            }
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
            System.exit(1);  // Exit with non-zero status to indicate failure
        }
    }
}
