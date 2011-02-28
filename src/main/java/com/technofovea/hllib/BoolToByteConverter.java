

package com.technofovea.hllib;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 *
 * @author Darien Hager
 */
class BoolToByteConverter implements TypeConverter{

    static final Byte BYTE_TRUE = 0x01;
    static final Byte BYTE_FALSE = 0x00;

    public Object fromNative(Object arg0, FromNativeContext arg1) {        
        Byte b = (Byte)arg0;
        if(b.equals(BYTE_TRUE)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
                
    }

    public Object toNative(Object arg0, ToNativeContext arg1) {
        Boolean b = (Boolean)arg0;
        if(b.equals(Boolean.TRUE)){
            return BYTE_TRUE;
        }else{
            return BYTE_FALSE;
        }
    }

    public Class nativeType() {
        return Byte.class;
    }
}
