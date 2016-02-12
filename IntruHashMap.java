/*
 (C) Paperhorse 2016
 MIT Licenced
 
 Intrusive HashMap (or maybe HashSet)
    (maybe not as intrusive as other algs 
     because of hashtable array)
 
*/
package com.countersort.paperhorse.intrusivejava;


public abstract class IntruHashMap<T> {
    
    public abstract T getNextLink(T e);
    public abstract void setNextLink(T e,T next);
    
    public abstract boolean equality(T o1, T o2);
    public abstract int hashCoder(T o1);
    
    Object[] hashTable;
    int[] primes={17,83,359,2027,9461,42083,142501,614377,2260961,
        10176347,57371983,236769133,747237137};
    int count;
    int primeIndex;
    final int maxload=10;
    
    public IntruHashMap() {
        clear();
    }
    
    public void clear() {
        count=0;
        primeIndex=0;
        hashTable=new Object[primes[primeIndex]];
    } 
    
    private void enlargeTable() {
        int i;
        int h;
        int p2;
        T q,n;
        p2=primes[++primeIndex];
        Object[] h2=new Object[p2];
        for (i=0;i<hashTable.length;i++) {
            q=castT(hashTable[i]);
            while (q!=null) {
                n=getNextLink(q);
                h=hashCoder(q);
                h=h & 0x7fffFFFF;
                h=h % p2;
                setNextLink(q,castT(h2[h]));
                h2[h]=q;
                q=n;
            }
        }
        hashTable=h2;
    }
    
    public void insert(T e) {
        int h;
        h=hashCoder(e);
        h=h & 0x7fffFFFF;
        h=h % hashTable.length;
        setNextLink(e, castT(hashTable[h]));
        hashTable[h]=e;
        count++;
        if (count/maxload > hashTable.length) 
            enlargeTable();
        
    }
    
    public void delete(T e) {
        int h;
        T q,p=null,n;
        h=hashCoder(e);
        h=h & 0x7fffFFFF;
        h=h % hashTable.length;
        q=castT(hashTable[h]);
        while (q!=null && q!=e) {
            p=q;
            q=getNextLink(q);
        }
        if (q==null) return; //add exception here
        n=getNextLink(q);
        if (p!=null) setNextLink(p,n);
        else hashTable[h]=n;
    }
    
    public T find(T key) {
        int h;
        T q;
        h=hashCoder(key);
        h=h & 0x7fffFFFF;
        h=h % hashTable.length;
        q=castT(hashTable[h]);
        while (q!=null && !equality(q,key))
            q=getNextLink(q);
        return q;
    }
    
    private T nextNonEmptyFromSlot(int i) {
        while (i<hashTable.length && hashTable[i]==null) i++;
        if (i>=hashTable.length) return null;
        else return castT(hashTable[i]);
    }
    
    public T iteratorStart() {
        return nextNonEmptyFromSlot(0);
    }
    
    public T iteratorNext(T e) {
        T q;
        int h;
        q=getNextLink(e);
        if (q!=null) return q;
        h=hashCoder(e);
        h=h & 0x7fffFFFF;
        h=h % hashTable.length;
        return nextNonEmptyFromSlot(h+1);
    }
    
    @SuppressWarnings("unchecked")
    T castT(Object o) {
        return (T)o;
    }
    
}

