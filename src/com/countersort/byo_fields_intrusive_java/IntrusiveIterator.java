
package com.countersort.byo_fields_intrusive_java;


public interface IntrusiveIterator<T> {
    T iterateNext(T e);
    T iteratePrevious(T e);
    T iterateStart();
    T iterateLast();
}
