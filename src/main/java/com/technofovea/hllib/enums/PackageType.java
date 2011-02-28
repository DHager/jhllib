package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum PackageType implements JnaEnum<PackageType> {

    NONE,
    BSP,
    GCF,
    PAK,
    VBSP,
    WAD,
    XZP,
    ZIP,
    NCF,
    VPK;
    
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }

    public PackageType getForValue(
            int i) {
        for (PackageType o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }

        }
        return null;
    }
}
