package com.technofovea.hllib;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

/**
 *
 * @author Darien Hager
 */
class StreamConverter implements TypeConverter {

    public Object fromNative(Object input, FromNativeContext context) {
        assert(context.getTargetType().equals(HlStream.class));
        Pointer ptr = (Pointer)input;
        return new HlStream(HlLib.getLibrary().getCurrentlyBoundPackage(),ptr);
    }

    public Object toNative(Object input, ToNativeContext context) {
        HlStream stream = (HlStream)input;
        // Check closed
        if(stream.isClosed()){
            throw new IllegalStateException("Stream cannot be used, it's owning package was closed.");
        }
        // Auto-rebind package to prevent an error
        if(HlLib.getLibrary().getCurrentlyBoundPackage() != stream.getParentPackage()){
            HlLib.getLibrary().bindPackage(stream.getParentPackage());
        }
        return stream.getPointer();
    }

    public Class nativeType() {
        return Pointer.class;
    }
}
