package com.technofovea.hllib.datatypes;

import com.sun.jna.Structure;

/**
 * If you try to do this as an inner-class, the Struct-size-detection code
 * mysteriously fails! Don't use inner classes if you can help it when it comes
 * to all this introspection magic...
 *
 * @author Darien Hager
 */
public class UnsignedStruct extends Structure {

    public int uiValue = 0;
    public byte bHexadecimal = 0x00;


}
