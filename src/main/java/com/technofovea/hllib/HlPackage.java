package com.technofovea.hllib;

/**
 * This class is used in place of an integer value that designates a package ID.
 * One good reason to use this is to guard against some JVM-crashing behavior
 * when a package is removed but the associated DirectoryItem pointers are used.
 * 
 * @author Darien Hager
 */
public class HlPackage{    

    static WeakReferenceCache<Integer,HlPackage> cache = new WeakReferenceCache<Integer, HlPackage>(){
        @Override
        public Integer getKey(HlPackage item) {
            return item.getID();
        }
    };
   
    public static HlPackage create(int i) {
        HlPackage cached = cache.getByKey(i);
        if(cached != null){
            return cached;
        }
        HlPackage pkg = new HlPackage(i);
        cache.store(pkg);
        return pkg;
    }
    

    boolean closed = false;
    int packageId = -1;


    /**
     * This constructor should not be used except internally by JNA code. Please
     * use the factory method create() instead.
     */
    HlPackage(Integer id) {
        this.packageId = id;
    }
    
    public int getID(){
        return packageId;
    } 

    public boolean isClosed() {
        return closed;
    }

    /**
     * This is a normal but irreversible operation which will render this package object and
     * all associated DirectoryItem java objects unusable with the libary interface.
     */
    void markClosed() {
        closed = true;        
        // Remove self from cache, or a new package that shares the same integer ID
        // might collide.
        cache.remove(this);
    }

   

    @Override
    public String toString() {
        return getClass().getSimpleName()+"#"+getID();
    }





    






}
