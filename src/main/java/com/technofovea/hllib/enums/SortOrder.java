package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum SortOrder implements JnaEnum<SortOrder> {

    ASCENDING,
    DESCENDING;
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public SortOrder getForValue(
            int i) {
        for (SortOrder o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }

        }
        return null;
    }
}
