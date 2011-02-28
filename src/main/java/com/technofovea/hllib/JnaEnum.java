package com.technofovea.hllib;

/**
 *
 * @author Darien Hager
 */
public interface JnaEnum<T> {
    public int getIntValue();
    public T getForValue(int i);
}
