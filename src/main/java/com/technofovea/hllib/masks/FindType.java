package com.technofovea.hllib.masks;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import java.util.BitSet;



/**
 *
 * @author Darien Hager
 */
public class FindType extends BitSet implements NativeMapped{
    	    	
        public static final int HL_FIND_FILES= 0;
	public static final int HL_FIND_FOLDERS= 1;
	public static final int HL_FIND_NO_RECURSE= 2;
	public static final int HL_FIND_CASE_SENSITIVE= 3;
	public static final int HL_FIND_MODE_STRING= 4;
	public static final int HL_FIND_MODE_SUBSTRING= 5;

    public int toInt(){
        int val =0;
        for(int i = 0; i < this.length(); i++){
            if(this.get(i)){
                val += (1<<i);
            }
        }
        return val;
    }

    public void fromInt(int intValue){
        this.clear();
        int max = 0;
        while(1<<max < intValue){
            max++;
        }

        for(int i=max; i > 0; i--){
            int part = 1<<i;
            if(part <= intValue){
                intValue -= part;
                this.set(i,true);
            }
        }
    }


    public Object toNative() {
        return this.toInt();
    }

    public Object fromNative(Object arg0, FromNativeContext arg1) {
        Integer i = (Integer)arg0;
        FindType fm = new FindType();
        fm.fromInt(i);
        return fm;
    }

    public Class nativeType() {
        return Integer.class;
    }


}
