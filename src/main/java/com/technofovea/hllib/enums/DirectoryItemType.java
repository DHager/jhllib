package com.technofovea.hllib.enums;

import com.technofovea.hllib.JnaEnum;

/**
 *
 * @author Darien Hager
 */
public enum DirectoryItemType implements JnaEnum<DirectoryItemType> {

    
    NONE,
    FOLDER,
    FILE;


    private static int start = 0;

    public int getIntValue() {
        return this.ordinal()+start;
    }


    public DirectoryItemType getForValue(int i) {
        for(DirectoryItemType o : this.values()){
            if(o.getIntValue() == i){
                return o;
            }
        }
        return null;
    }




}
