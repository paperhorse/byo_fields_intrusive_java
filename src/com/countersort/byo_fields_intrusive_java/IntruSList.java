/*
 (C) Paperhorse 2016
 MIT Licenced
 
 Intrusive Singly Linked List
 
*/

package com.countersort.byo_fields_intrusive_java;


import java.util.Comparator;
import java.util.Iterator;
import com.countersort.byo_fields_intrusive_java.CompatibleSListIterator;

/**
<p>
A generic intrusive singly linked list. Use by defining a subclass
with appropriate getter and setter fields defined over the abstract 
methods which reach into the Node objects to read/write the next pointer
for this list.
</p>

@param <T> The type of the nodes in the singly linked list. Must contain
a next pointer which is &quot;wired in&quot; with getter and setters
from de-abstracted IntruSList. If the Node is in several lists these 
might be named &quot;next1&quot;, &quot;next2&quot;, 
&quot;next3&quot;, etc.    
*/


public abstract class IntruSList<T> implements Iterable<T> {
    T head;
    T tail;
    long count;

    public abstract T getNextLink(T e);
    public abstract void setNextLink(T e,T next);
    /**
    Clears the current list
    */
    public void clear() {
        head=tail=null;
        count=0;
    }
    
    /**
    Puts the new node at the beginning of
    the list.
    @param e the new node to be inserted.
    */
    
    public void prepend(T e) {
        count++;
        setNextLink(e,head);
        if (head==null) tail=e;
        head=e;
    }
    /**
    Removes a node from the beginning of the list and returns it.
    
    @return First element of list
    
    */
    public T extractFirst() {
        T extract;
        extract=head;
        if (extract!=null) {
            head=getNextLink(head);
            count--;
        }
        if (head==null) tail=null;
        return extract;
    }
    
    /**
    Inserts a new element after gien element
    
    @param existingElement  the new element is placed after this element.
            Can be null indicating newElement is placed at start of list.
    @param newElement  Node inserted into list.
    */

    public void insertAfter(T existingElement, T newElement) {
        T n;
        count++;
        if (existingElement!=null) {
            n=getNextLink(existingElement);
            setNextLink(existingElement,newElement);
        } else {
            n=head;
            head=newElement;
        }
        setNextLink(newElement,n);
        if (n==null) tail=newElement;
    }
    
    /**
    puts a new node at the end of the list
    
    @param e node to be appended.
    */
    
    public void append(T e) {
        count++;
        setNextLink(e, null);
        if (tail!=null) {
            setNextLink(tail,e);
            tail=e;
        } else {
            head=tail=e;
        }
    }
    
    /**
    Deletes a node following the given node.
    
    @param before Node before the node to be deleted
    */
    
    public void deleteAfter(T before) {
        T n,nn;
        if (before!=null) {
            n=getNextLink(before);
            if (n==null) return;
            nn=getNextLink(n);
            setNextLink(before,nn);
        } else {
            n=head;
            if (n==null) return;
            head=nn=getNextLink(n);
        }
        if (nn==null) tail=before;
        count--;
    }
    
    
    /**
    @param e current place in list.
    @return next element in list after given node.
    */
    public T iterateNext(T e) {
        return getNextLink(e);
    }
    /**
    @return start of list
    */
    public T iterateStart() {
        return head;
    }
    
    /**
    Returns a java compatible iterator you can use with
    the for each syntax.
    */
    @Override
    public Iterator<T> iterator() {
        return new CompatibleSListIterator(this);
    } 
}
