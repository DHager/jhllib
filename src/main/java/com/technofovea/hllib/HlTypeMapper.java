package com.technofovea.hllib;

import com.sun.jna.DefaultTypeMapper;

/**
 *
 * @author Darien Hager
 */
class HlTypeMapper extends DefaultTypeMapper {

    HlTypeMapper() {
        addTypeConverter(Boolean.class, new BoolToByteConverter());
        addTypeConverter(JnaEnum.class, new EnumConverter());
        addTypeConverter(HlPackage.class, new PackageConverter());
        addTypeConverter(DirectoryItem.class, new DirItemConverter());
        addTypeConverter(HlStream.class, new StreamConverter());
    }
}
