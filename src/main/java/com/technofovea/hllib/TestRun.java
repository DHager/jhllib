package com.technofovea.hllib;

import com.technofovea.hllib.enums.HlOption;
import com.technofovea.hllib.methods.ManagedLibrary;

/**
 *
 * @author Darien Hager
 */
public class TestRun {

    public static void main(String[] args){
        System.out.println("Attempting to load HlLib...");
        ManagedLibrary lib = HlLib.getLibrary();
        System.out.println("Retrieving version...");
        String version = lib.getString(HlOption.VERSION);
        System.out.println("Version detected as '"+version+"'");

    }
}
