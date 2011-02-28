
package com.technofovea.hllib.masks;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import java.util.BitSet;

/**
 *
 * @author Darien Hager
 */
public abstract class MappedMask extends BitSet implements NativeMapped {

    public int getInt() {
        int val = 0;
        for (int i = 0; i < this.length(); i++) {
            if (this.get(i)) {
                val += (1 << i);
            }
        }
        return val;
    }

    public void setInt(int intValue) {
        this.clear();
        int max = 0;
        while (1 << max < intValue) {
            max++;
        }

        for (int i = max; i > 0; i--) {
            int part = 1 << i;
            if (part <= intValue) {
                intValue -= part;
                this.set(i, true);
            }
        }
    }

    public Object toNative() {
        return this.getInt();
    }

    public Object fromNative(Object arg0, FromNativeContext arg1) {
        Integer i = (Integer) arg0;
        this.setInt(i);
        return this;
    }

    public Class nativeType() {
        return Integer.class;
    }
}
