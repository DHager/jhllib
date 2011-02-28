package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum Validation implements JnaEnum<Validation> {

    OK,
    ASSUMED_OK,
    INCOMPLETE,
    CORRUPT,
    CANCELED,
    ERROR;
    
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public Validation getForValue(
            int i) {
        for (Validation o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }

        }
        return null;
    }
}
