package com.technofovea.hllib;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Darien Hager
 */

public class GcfFinder {

    final static String GCF_NAME = "half-life engine.gcf";

    /**
     * Until there is a way to create our own GCF for testing, we have to test against
     * GCFs which are copyrighted and thus cannot be redistributed. This function
     * makes it easy to warn people when their test environment is not set up correctly.
     * 
     * @return An URL reference to the test file.
     * @throws FileNotFoundException If the file could not be found.
     */
    public static URL getTestGcf() throws FileNotFoundException, URISyntaxException {
        URL url = GcfFinder.class.getResource(GCF_NAME);
        if (url == null) {
            throw new FileNotFoundException("Could not find test.gcf. This file is NOT present in the source repository, because it is owned by Valve Software. Please copy '" + GCF_NAME + "' from your Steam folder manually.");
        }
        return url;
    }
}
