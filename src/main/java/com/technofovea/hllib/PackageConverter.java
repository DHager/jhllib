package com.technofovea.hllib;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Darien Hager
 */
class PackageConverter implements TypeConverter {

    private static final Logger logger = LoggerFactory.getLogger(PackageConverter.class);

    public Object fromNative(Object input, FromNativeContext context) {
        assert (context.getTargetType().equals(HlPackage.class));
        Integer i = (Integer) input;
        if (i < 0) {
            logger.error("Could not convert package from negative native value {}",i);
            return null; //TODO better way to report errors?
        }
        HlPackage pkg = HlPackage.create(i);
        return pkg;

    }

    public Object toNative(Object input, ToNativeContext context) {
        HlPackage pkg = (HlPackage) input;
        return pkg.getID();
    }

    public Class nativeType() {
        return Integer.class;
    }
}
