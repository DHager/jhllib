package com.technofovea.hllib;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Darien Hager
 */
class DirItemConverter implements TypeConverter {

    private static final Logger logger = LoggerFactory.getLogger(DirItemConverter.class);

    public Object fromNative(Object input, FromNativeContext context) {
        assert (context.getTargetType().equals(DirectoryItem.class));
        Pointer ptr = (Pointer) input;
        if (ptr == null || ptr.equals(Pointer.NULL)) {
            return null;
        }
        return new DirectoryItem(HlLib.getLibrary().getCurrentlyBoundPackage(), ptr);
    }

    public Object toNative(Object input, ToNativeContext context) {
        DirectoryItem di = (DirectoryItem) input;
        // Check closed
        if (di.isClosed()) {
            throw new IllegalStateException("DirectoryItem cannot be used, it's owning package ("+di.getParentPackage().getID()+") was closed.");
        }
        // Auto-rebind package to prevent an error
        HlPackage curPackage = HlLib.getLibrary().getCurrentlyBoundPackage();
        HlPackage targetPackage = di.getParentPackage();
        if (!curPackage.equals(targetPackage)) {
            logger.debug("Automatically rebinding library's current package (from {} to {}) to handle DirectoryItem {}",new Object[]{curPackage,targetPackage,di});
            HlLib.getLibrary().bindPackage(di.getParentPackage());
        }
        return di.getPointer();
    }

    public Class nativeType() {
        return Pointer.class;
    }
}
