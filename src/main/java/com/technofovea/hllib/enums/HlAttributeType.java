
package com.technofovea.hllib.enums;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum HlAttributeType implements JnaEnum<HlAttributeType> {

    INVALID,
    BOOLEAN,
    INTEGER,
    UNSIGNED_INTEGER,
    FLOAT,
    STRING;

    private static int start = 0;

    public int getIntValue() {
        return this.ordinal()+start;
    }


    public HlAttributeType getForValue(int i) {
        for(HlAttributeType o : this.values()){
            if(o.getIntValue() == i){
                return o;
            }
        }
        return null;
    }






}
