package com.technofovea.hllib;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * A class to manage the caching of Java objects via references which do not
 * prevent garbage collection from occurring.
 *
 * The main purpose is to provide a consistent set of objects which wrap JNA
 * constructs such as Pointers, since JNA, undisturbed, tends to allocate a new
 * object every time rather than reusing old ones.
 *
 * This class is not thread-safe.
 * @author Darien Hager
 */
public abstract class WeakReferenceCache<K,V> {
    protected HashMap<K,WeakReference<V>> map = new HashMap<K,WeakReference<V>>();

    /**
     * Determines the key used to avoid duplicates of the item.
     * 
     * @param item The item to inspect
     * @return A key used to prevent duplicates
     */
    public abstract K getKey(V item);


    /**
     * Removes the entry in the cache which corresponds to the given object,
     * even if the two are not technically the same instance.
     *      
     * @param item The item to remove or a similar one.
     * @return True if something was removed, false otherwise.
     */
    public boolean remove(V item){
        K key = getKey(item);
        if(map.containsKey(key)){
            map.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Find a cached copy that matches the given object and returns it. If the
     * cache is empty, the given object is stored and returned.
     * @param item The item to try to store
     * @return The item which was previous cached or just-now cached.
     */
    public V getOrPut(V item){
        K key = getKey(item);        
        V stored = getByKey(key);
        if(stored != null){
            return stored;
        }else{
            store(item);
            return item;
        }
    }


    /**
     * Attempts to store the given item into the cache. Will return false
     * if another item is already present for the asme key.
     * @param candidate Item to store
     * @return True if it was stored successfully.
     */
    public boolean store(V candidate){
        K key = getKey(candidate);
        V stored = getByKey(key);
        if(stored != null){
            return false;
        }else{
            map.put(key, new WeakReference<V>(candidate));
            return true;
        }
    }


    /**
     * Looks to see if the given object would correspond to an item in the cache.
     * If such an item exists, it is retrieved and returned.
     * @param candidate
     * @return The cached object, or null if nothing found.
     */
    public V get(V candidate){
        V stored = getByKey(getKey(candidate));
        if(stored != null){
            return stored;
        }else{
            return null;
        }
    }
    /**
     * Attempts to retrieve a cached copy for the given key. Will return null
     * if nothing could be found or the object was garbage-collected.
     * @param key Key to search for
     * @return The stored object or null if none found.
     */
    public V getByKey(K key){
        if(!map.containsKey(key)){
            return null;
        }
        WeakReference<V> ref = map.get(key);
        V stored = ref.get();
        if(stored == null){
            map.remove(key);
            return null;
        }
        return stored;
    }

    /**
     * Removes all entries where the reference is no longer valid.
     */
    public void clean(){
        for(K key: map.keySet()){
            if(map.get(key).get() == null){
                map.remove(key);
            }
        }
    }
}
