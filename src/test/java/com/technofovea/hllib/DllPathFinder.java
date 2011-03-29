package com.technofovea.hllib;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * Quick utility class designed to allow tests to initialize the library even if
 * the current directory is "below" the place where the DLLs are stored. 
 * 
 * This is primarily to allow IDEs to run individual tests (and debug them) 
 * rather than having to run the entire suite via maven.
 *
 * @author Darien Hager
 */
public class DllPathFinder {

    private static class DllFilter implements FilenameFilter{
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".dll");
        }
    }
    private static DllFilter filter = new DllFilter();

    public static boolean setJnaPathPath() throws URISyntaxException{
        URI startPos = FindingTest.class.getClassLoader().getResource(".").toURI();
        File current = new File(startPos);
        File found = null;
        // Move towards the root of the filesystem until a DLL is found
        while((current != null) && current.isDirectory()){
            if(current.listFiles(filter).length > 0){
                found = current;
                break;
            }else{
                current = current.getParentFile();
            }
        }
        if(current != null){
            System.setProperty("jna.library.path",current.getAbsolutePath());
            return true;
        }else{
            return false;
        }
        

    }
}
