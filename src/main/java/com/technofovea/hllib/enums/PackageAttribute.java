
package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 * This is trickier than our normal enums because there seems to be overlap:
 * Multiple enums will resolve to the same integer. Weird! Thankfully nothing
 * ever returns these numbers from the library, so we do not need to deal with
 * finding the correct enum that matches zero.
 * @author Darien Hager
 */
public enum PackageAttribute implements JnaEnum<PackageAttribute> {

    BSP_PACKAGE_VERSION(true), //=0
    BSP_PACKAGE_COUNT,

    BSP_ITEM_WIDTH(true), //=0
    BSP_ITEM_HEIGHT,
    BSP_ITEM_PALETTE_ENTRIES,
    BSP_ITEM_COUNT,

    GCF_PACKAGE_VERSION(true), //=0
    GCF_PACKAGE_ID,
    GCF_PACKAGE_ALLOCATED_BLOCKS,
    GCF_PACKAGE_USED_BLOCKS,
    GCF_PACKAGE_BLOCK_LENGTH,
    GCF_PACKAGE_LAST_VERSION_PLAYED,
    GCF_PACKAGE_COUNT,

    GCF_ITEM_ENCRYPTED(true), //=0
    GCF_ITEM_COPY_LOCAL,
    GCF_ITEM_OVERWRITE_LOCAL,
    GCF_ITEM_BACKUP_LOCAL,
    GCF_ITEM_FLAGS,
    GCF_ITEM_FRAGMENTATION,
    GCF_ITEM_COUNT,

    NCF_PACKAGE_VERSION(true), //=0
    NCF_PACKAGE_ID,
    NCF_PACKAGE_LAST_VERSION_PLAYED,
    NCF_PACKAGE_COUNT,

    NCF_ITEM_ENCRYPTED(true), //=0
    NCF_ITEM_COPY_LOCAL,
    NCF_ITEM_OVERWRITE_LOCAL,
    NCF_ITEM_BACKUP_LOCAL,
    NCF_ITEM_FLAGS,
    NCF_ITEM_COUNT,

    PAK_PACKAGE_COUNT(true), //=0

    PAK_ITEM_COUNT(true), //=0

    VBSP_PACKAGE_VERSION(true), //=0
    VBSP_PACKAGE_MAP_REVISION,
    VBSP_PACKAGE_COUNT,

    VBSP_ITEM_VERSION(true), //=0
    VBSP_ITEM_FOUR_CC,
    VBSP_ZIP_PACKAGE_DISK,
    VBSP_ZIP_PACKAGE_COMMENT,
    VBSP_ZIP_ITEM_CREATE_VERSION,
    VBSP_ZIP_ITEM_EXTRACT_VERSION,
    VBSP_ZIP_ITEM_FLAGS,
    VBSP_ZIP_ITEM_COMPRESSION_METHOD,
    VBSP_ZIP_ITEM_CRC,
    VBSP_ZIP_ITEM_DISK,
    VBSP_ZIP_ITEM_COMMENT,
    VBSP_ITEM_COUNT,

    VPK_PACKAGE_ARCHIVES(true), //=0
    VPK_PACKAGE_VERSION,
    VPK_PACKAGE_COUNT,

    VPK_ITEM_PRELOAD_BYTES(true), //=0
    VPK_ITEM_ARCHIVE,
    VPK_ITEM_CRC,
    VPK_ITEM_COUNT,

    WAD_PACKAGE_VERSION(true), //=0
    WAD_PACKAGE_COUNT,

    WAD_ITEM_WIDTH(true), //=0
    WAD_ITEM_HEIGHT,
    WAD_ITEM_PALETTE_ENTRIES,
    WAD_ITEM_MIPMAPS,
    WAD_ITEM_COMPRESSED,
    WAD_ITEM_TYPE,
    WAD_ITEM_COUNT,

    XZP_PACKAGE_VERSION(true), //=0
    XZP_PACKAGE_PRELOAD_BYTES,
    XZP_PACKAGE_COUNT,

    XZP_ITEM_CREATED(true), //=0
    XZP_ITEM_PRELOAD_BYTES,
    XZP_ITEM_COUNT,

    ZIP_PACKAGE_DISK(true), //=0
    ZIP_PACKAGE_COMMENT,
    ZIP_PACKAGE_COUNT,
    
    ZIP_ITEM_CREATE_VERSION(true), //=0
    ZIP_ITEM_EXTRACT_VERSION,
    ZIP_ITEM_FLAGS,
    ZIP_ITEM_COMPRESSION_METHOD,
    ZIP_ITEM_CRC,
    ZIP_ITEM_DISK,
    ZIP_ITEM_COMMENT,
    ZIP_ITEM_COUNT;
    
    
    static{
        // Responsible for setting number values
        int counter = 0;
        for(PackageAttribute p : PackageAttribute.values()){
            if(p.resetToZero){
                counter = 0;
            }
            p.value = counter;
            counter++;
        }
    }

    private int value = 0; // Set by static block
    private boolean resetToZero = false; // If this enum resets the counter

    private PackageAttribute() {
        resetToZero = false;
    }
    private PackageAttribute(boolean resetCounter){
        resetToZero = resetCounter;
    }



    public int getIntValue() {
        return value;
    }

    public PackageAttribute getForValue(int i) {
        throw new UnsupportedOperationException("These enum choices have collisions when converted from integer values.");
    }
}
