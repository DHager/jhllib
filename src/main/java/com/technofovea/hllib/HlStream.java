package com.technofovea.hllib;

import com.sun.jna.Pointer;

/**
 *
 * @author Darien Hager
 */
public class HlStream {


    Pointer pointer = null;
    HlPackage parentPackage = null;

    
    /**
     * This constructor should not be used except internally by JNA code.
     */
    HlStream(HlPackage parent,Pointer p) {
        pointer = p;
        parentPackage = parent;
    }

    public boolean isClosed() {
        return parentPackage.isClosed();
    }

    public Pointer getPointer() {
        return pointer;
    }

    public HlPackage getParentPackage() {
        return parentPackage;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HlStream)){
            return false;
        }
        HlStream other = (HlStream)obj;
        if(!parentPackage.equals(other.parentPackage)){
            return false;
        }
        if(!pointer.equals(other.pointer)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return pointer.hashCode() + parentPackage.hashCode();
    }


    
}
