package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum HlOption implements JnaEnum<HlOption> {

    VERSION,
    ERROR,
    ERROR_SYSTEM,
    ERROR_SHORT_FORMATED,
    ERROR_LONG_FORMATED,
    PROC_OPEN,
    PROC_CLOSE,
    PROC_READ,
    PROC_WRITE,
    PROC_SEEK,
    PROC_TELL,
    PROC_SIZE,
    PROC_EXTRACT_ITEM_START,
    PROC_EXTRACT_ITEM_END,
    PROC_EXTRACT_FILE_PROGRESS,
    PROC_VALIDATE_FILE_PROGRESS,
    OVERWRITE_FILES,
    PACKAGE_BOUND,
    PACKAGE_ID,
    PACKAGE_SIZE,
    PACKAGE_TOTAL_ALLOCATIONS,
    PACKAGE_TOTAL_MEMORY_ALLOCATED,
    PACKAGE_TOTAL_MEMORY_USED,
    READ_ENCRYPTED,
    FORCE_DEFRAGMENT,
    PROC_DEFRAGMENT_PROGRESS,
    PROC_DEFRAGMENT_PROGRESS_EX,;
    
    private static int start = 0;

    public int getIntValue() {
        return this.ordinal() + start;
    }


    public HlOption getForValue(int i) {
        for (HlOption o : this.values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }
    
}
