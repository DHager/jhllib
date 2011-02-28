package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum SortField implements JnaEnum<SortField> {

    FIELD_NAME,
    FIELD_SIZE;
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public SortField getForValue(
            int i) {
        for (SortField o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }

        }
        return null;
    }
}