/*
 (C) Paperhorse 2016
 MIT Licenced
*/

package com.countersort.byo_fields_intrusive_java;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;

public class CompatibleSListIterator<T> implements Iterator<T> {
    
    IntruSList<T> container;
    T element;
    T preDelete;
    boolean deleted;
    
    CompatibleSListIterator(IntruSList<T> container) {
        this.container=container;
        element=container.iterateStart();
        preDelete=null;
        deleted=true;
    }
    
    @Override
    public boolean hasNext() {
        return (element!=null);
    }
    
    @Override
    public T next() {
        T e=element;
        T p=null, q=null;
        if (e==null) throw new NoSuchElementException();
        element=container.iterateNext(e);
        q=preDelete;
        while (q!=e) {
            p=q;
            if (q==null) q=container.iterateStart();
            else q=container.iterateNext(q);
        }
        preDelete=p;
        deleted=false;
        return e;
    }

    @Override
    public void remove() {
        if (deleted) throw new UnsupportedOperationException();
        container.deleteAfter(preDelete);
        deleted=true;
    }
    
} 
