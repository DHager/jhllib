package com.technofovea.hllib.masks;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import java.util.BitSet;



/**
 *
 * @author Darien Hager
 */
public class FileMode extends MappedMask{
    
	public static final int INDEX_MODE_READ= 0;
	public static final int INDEX_MODE_WRITE= 1;
	public static final int INDEX_MODE_CREATE= 2;
	public static final int INDEX_MODE_VOLATILE= 3;
	public static final int INDEX_MODE_NO_FILEMAPPING= 4;
	public static final int INDEX_MODE_QUICK_FILEMAPPING= 5;

}
