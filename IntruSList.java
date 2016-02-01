/*
 (C) Paperhorse 2016
 MIT Licenced
 
 Intrusive Single Linked List
 
*/

import java.util.Comparator;


public abstract class IntruSList<T> {
    T head;
    T tail;
    long count;

    public abstract T getNextLink(T e);
    public abstract void setNextLink(T e,T next);

    public void prepend(T e) {
        count++;
        setNextLink(e,head);
        if (head==null) tail=e;
        head=e;
    }
    
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

    public void insertAfter(T existingElement, T newElement) {
        T n;
        count++;
        n=getNextLink(existingElement);
        setNextLink(existingElement,newElement);
        setNextLink(newElement,n);
        if (n==null) tail=newElement;
    }

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
    
    public void deleteAfter(T before) {
        T n,nn;
        n=getNextLink(before);
        if (n==null) return;
        nn=getNextLink(n);
        setNextLink(before,nn);
        count--;
    }

    public T iterateNext(T e) {
        return getNextLink(e);
    }

    public T iterateStart() {
        return head;
    }
}