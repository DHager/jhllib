package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum StreamType implements JnaEnum<StreamType> {

        NONE,
	FILE,
	GCF,
	MAPPING,
	MEMORY,
	PROC,
	NULL;
    
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public StreamType getForValue(int i) {
        for (StreamType o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }
}
