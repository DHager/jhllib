
package com.technofovea.hllib.datatypes;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.technofovea.hllib.enums.HlAttributeType;

/**
 * This class is used to retrieve "Attribute" values from HLLib. It's a bit
 * tricky.
 *
 * Generally speaking you should only need to use the following methods:
 * getType()
 * getJavaName()
 * getJavaData()
 * 
 * @author Darien Hager
 */
public class Attribute extends Structure implements Structure.ByReference {

    public static class AttributeUnion extends Union {
        /*
        public boolean Boolean = false;
        public int Integer = 0;        
        public int UnsignedInteger = 0;
        public float Float = 0;
        public byte[] String = new byte[252];
         */
        //public BooleanStruct b = new BooleanStruct();

        public boolean b = false;
        public int i = 0;
        public UnsignedStruct ui = new UnsignedStruct();
        public float f = 0;
        public byte[] s = new byte[256];
    }
    public int attribTypeId; // Ordinal of corresponding HlAttributeType
    //public HlAttributeTypeHolder attrib;

    //public int attribId;
    public byte[] nameBuf = new byte[252];
    public AttributeUnion Value = new AttributeUnion();

    
    private void doRead(HlAttributeType type) {
        switch (type) {
            case BOOLEAN:
                Value.readField("b");
                break;
            case INTEGER:
                Value.readField("i");
                break;
            case UNSIGNED_INTEGER:
                Value.readField("ui");
                break;
            case FLOAT:
                Value.readField("f");
                break;
            case STRING:
                Value.readField("s");
                break;
        }
    }

    /**
     * Returns the type of the payload as an enum. The raw integer type is in attribTypeId.     
     * @return The enum type, or null if the integer value was not recognized.
     */
    public HlAttributeType getType() {
        HlAttributeType type = HlAttributeType.BOOLEAN.getForValue(attribTypeId);
        return type;
    }

    /**
     * Get the name of this attribute as a java String.
     * @return The name of the attribute held by this structure.
     */
    public String getJavaName() {
        return Native.toString(nameBuf);
    }

    /**
     * Converts and returns the appropriate data held this struct, depending on
     * the type information (accessible via getType()). Returns null on errors
     * or if the getType() is invalid.
     *
     * @return May return Integer, Boolean, Float, or String. Null on error.
     */
    public Object getJavaData() {
        HlAttributeType type = getType();
        if (type == null) {
            //TODO log
            return null;
        }
        doRead(type);

        switch (type) {
            case BOOLEAN:
                return Value.b;
            case INTEGER:
                return Value.i;
            case UNSIGNED_INTEGER:
                return Value.ui.uiValue;
            case FLOAT:
                return Value.f;
            case STRING:
                return Native.toString(Value.s);
            default:
                //TODO log
                return null;

        }

    }

    

    /**
     * Useful only for a niche condition where you need to retrieve UNSIGNED_INTEGER
     * data. Returns whether or not the data was marked as "hex". If the current
     * data type is different, will return false.
     * @return True if the integer should be depicted as hex, false otherwise.         
     */
    public boolean unsignedIsHex() {
        HlAttributeType type = getType();
        if (type == null) {
            return false;
        }

        if(type == HlAttributeType.UNSIGNED_INTEGER){
            doRead(type);
            byte original = Value.ui.bHexadecimal;
            if(original == 0x00){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
}
