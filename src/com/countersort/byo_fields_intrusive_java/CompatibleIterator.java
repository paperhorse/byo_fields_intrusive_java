
package com.countersort.byo_fields_intrusive_java;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;

import com.countersort.byo_fields_intrusive_java.IntrusiveIterator;

public class CompatibleIterator<T> implements Iterator<T> {
    
    IntrusiveIterator<T> it;
    T element; 
    CompatibleIterator(IntrusiveIterator<T> it) {
        this.it=it;
        element=it.iterateStart();
    }
    
    @Override
    public boolean hasNext() {
        return (element!=null);
    }
    
    @Override
    public T next() {
        T e=element;
        if (e==null) throw new NoSuchElementException();
        element=it.iterateNext(e);
        return e;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
} 
