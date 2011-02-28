package com.technofovea.hllib.masks;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum SeekMode implements JnaEnum<SeekMode> {

    BEGINNING,
    CURRENT,
    END;
    
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public SeekMode getForValue(
            int i) {
        for (SeekMode o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }

        }
        return null;
    }
}
