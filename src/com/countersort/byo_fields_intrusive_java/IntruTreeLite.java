
/*

 (C) Paperhorse 2016
 MIT Licenced

Scapegoat tree based intrusive TreeMap
Lightweight doesn't have parent links
 
*/

package com.countersort.byo_fields_intrusive_java;


public abstract class IntruTreeLite<T> {

    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    public abstract int compare(T o1, T o2);

    long count;
    long maxcount;
    double alpha=0.6; //should be between 0.51 (balanced) and 0.99
    T root;
    
    //final boolean FALSE=false;
    
    public void clear() {
        root=null;
        count=maxcount=0;
    }
    
    public T find(T key) {
        int c;
        T q=root;
        while (q!=null) {
            c=compare(q,key);
            if (c==0) return q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
        }
        return null;
    }
    
    static final int LT=1;
    static final int EQ=2;
    static final int GT=4;
    
    public T find_lt_eq_gt(T key, int lt_eq_gt) {
        T gt=null, lt=null, eq=null;
        int c;
        T q=root;
        while (q!=null) {
            c=compare(q,key);
            if (c==0) break;
            if (c>0) {gt=q;q=getLeftLink(q);}
            else {lt=q;q=getRightLink(q);}
        }
        eq=q;
        if ((lt_eq_gt & EQ)!=0 && eq!=null) return eq;
        if ((lt_eq_gt & GT)!=0 && gt!=null) return gt;
        if ((lt_eq_gt & LT)!=0 && lt!=null) return lt;
        return null;
    }
    
    
    public void insert(T e) {
        //System.out.println("INSERT "+e);
        long oldcount=++count;
        setLeftLink(e, null);
        setRightLink(e, null);
        count=0; //count now used as subtree count when search for scapegoat tree
        root=insert(root,e,1.0*oldcount);
        count=oldcount;
        if (count>maxcount) maxcount=count;
    }

    /*
    T insert(T q, T e, double alphacount) {
        T r=insertX(q,e,alphacount);
        System.out.println("insert returns");
        printTree(r);
        System.out.println();
        return r;
    }
    */
    
    public T insert(T q, T e, double alphacount) {
        int c; 
        long otherCount=0;
        T ch;
        if (q!=null) {
            alphacount*=alpha;
            c=compare(q,e);
            if (c>0) {//left
                ch=insert(getLeftLink(q),e,alphacount);
                setLeftLink(q,ch);
                if (count>0)
                    otherCount=countTree(getRightLink(q)); 
            } else {//right
                ch=insert(getRightLink(q),e,alphacount);
                setRightLink(q,ch);
                if (count>0)
                    otherCount=countTree(getLeftLink(q)); 
            }
            //check for rebalance;
            if (count>0 && (otherCount+count+1)*alpha < (double) count) {
                q=rebalance(q,otherCount+count+1);
                count=0;
            } else if (count>0) count+=1+otherCount;
            return q;
        } else {
            if (alphacount<1.0) count=1;
            return e;
        }
    }
    
    long countTree(T q) {
        long c=0;
        while (q!=null) {
            c+=1+countTree(getRightLink(q));
            q=getLeftLink(q);
        }
        return c;
    }
    
    T rebalance(T q, long cnt) {
        T oldroot=root;
        root=q;
        q=rebalance(cnt);
        root=oldroot;
        return q;
    } 
    
    T rebalance(long cnt) {
        T l,r,q;
        T tl,tlr;
        if (cnt<=0) return null;
        cnt--;
        l=rebalance(cnt/2);
        while (getLeftLink(root)!=null) {
            //rotate tree
            tl=getLeftLink(root);
            tlr=getRightLink(tl);
            setRightLink(tl,root);
            setLeftLink(root,tlr);
            root=tl;
        }
        q=root;
        root=getRightLink(root);
        r=rebalance(cnt-cnt/2);
        setLeftLink(q,l);
        setRightLink(q,r);
        return q;
    }
    
    public T iterateStart() {
        return leftmost(root);
    }
    
    public T iterateNext(T e) {
        T q,n=null;
        int c;
        if ((q=getRightLink(e))!=null)
            return leftmost(q);
        q=root;
        while (q!=e) {
            c=compare(q,e);
            if (c>0) {n=q;q=getLeftLink(q);}
            else q=getRightLink(q);
        }
        return n;
    }
    
    public T iterateLast() {
        return rightmost(root);
    }
    
    public T iteratePrevious(T e) {
        T q,n=null;
        int c;
        if ((q=getLeftLink(e))!=null)
            return rightmost(q);
        q=root;
        while (q!=e) {
            c=compare(q,e);
            if (c>0) q=getLeftLink(q);
            else {n=q;q=getRightLink(q);}
        }
        return n;
    }
    
    T leftmost(T q) {
        T p=null;
        while (q!=null) {
            p=q;
            q=getLeftLink(q);
        }
        return p;
    }
    
    T rightmost(T q) {
        T p=null;
        while (q!=null) {
            p=q;
            q=getRightLink(q);
        }
        return p;
    }
    
    public void delete(T e) {
        deleteNode(e);
        count--;
        if (1.0*count<alpha*maxcount && count>1) {
            root=rebalance(root,count);
            maxcount=count;
        }
    }
    
    void deleteNode(T e) {
        T q,p=null;
        T lp=null;
        int c=0;
        q=root;
        while (q!=e) {
            c=compare(q,e);
            p=q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
        }
        if (getLeftLink(e)==null) {
            q=getRightLink(e);
            if (p==null) root=q;
            else if (c>0) setLeftLink(p,q);
            else setRightLink(p,q);
            return;
        }
        if (getRightLink(e)==null) {
            q=getLeftLink(e);
            if (p==null) root=q;
            else if (c>0) setLeftLink(p,q);
            else setRightLink(p,q);
            return;
        }
        //internal node
        //delete predecessor
        lp=e;
        q=getLeftLink(e);
        while (getRightLink(q)!=null) {
            lp=q;
            q=getRightLink(q);
        }
        if (lp==e) setLeftLink(lp,getLeftLink(q));
        else setRightLink(lp,getLeftLink(q));
        //and replace it (q) over internal node (e)
        setLeftLink(q,getLeftLink(e));
        setRightLink(q,getRightLink(e));
        
        if (p==null) root=q;
        else if (c>0) setLeftLink(p,q);
        else setRightLink(p,q);
        
    }
    
    void printTree() {
        printTree(root,0);
    }
    void printTree(T q) {
        printTree(q,0);
        System.out.println();
    }
    
    void printTree(T q,int indent) {
        if (q==null) return;
        printTree(getLeftLink(q),indent+4);
        for (int i=0;i<indent;i++) System.out.print(" ");
        System.out.println(q);
        printTree(getRightLink(q),indent+4);
    }
    
}
