/*
 (C) Paperhorse 2016
 MIT Licenced

 Intrusive Priority Queue 
*/

package com.countersort.byo_fields_intrusive_java;

public abstract class IntruPriorityQueue<T> {

    public abstract T getParentLink(T e);
    public abstract void setParentLink(T e,T parent);
    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    public abstract int compare(T o1, T o2);

    T root;
    int count;
    
    public void clear() {
        root=null;
        count=0;
    }
    
    public void insert(T e) {
        T p,q, tmp;
        T l,r;
        q=root;
        p=null;
        while (q!=null) {
            l=getLeftLink(q);
            r=getRightLink(q);
            if (compare(e,q)<0) {
                //replacing q with e
                setParentLink(e,p);
                if (p==null) root=e;
                else setRightLink(p,e);
                if (l!=null) setParentLink(l,e);
                if (r!=null) setParentLink(r,e);
                tmp=q;q=e;e=tmp;
            }
            //swapping left/right
            setRightLink(q,l);
            setLeftLink(q,r);
            p=q;
            q=l; //descend old left link/current right link
        }
        if (p==null) root=e;
        else setRightLink(p,e);
        setParentLink(e,p);
        setLeftLink(e,null);
        setRightLink(e,null);
        count++;
    }
    
    public T extractTop() {
        T top, right;
        if (count<=0) return null;
        count--;
        top=root;
        right=removeRightmost();
        if (top==right) {
            root=null;
            return top;
        }
        replace(top, right);
        siftDown(right);
        return top;
    }
    
    public void changePriority(T e) {
        T p=getParentLink(e);
        if (p!=null && compare(e,p)<0) siftUp(e);
        else siftDown(e);
    }
    
    public void delete(T e) {
        T right=removeRightmost();
        if (right==null || e==right) return;
        replace(e,right);
        changePriority(right);
    }
    
    T removeRightmost() {
        T q,l,r,p;
        if (root==null) return null;
        q=root;
        p=null;
        while (q!=null) {
            l=getLeftLink(q);
            r=getRightLink(q);
            setLeftLink(q,r);
            setRightLink(q,l);
            p=q;
            q=r;
        }
        q=p;
        p=getParentLink(q);
        if (p==null) root=null;
        else setLeftLink(p,null);
        return q;
    }
    
    void replace(T older, T newer) {
        T p,l,r;
        p=getParentLink(older);
        l=getLeftLink(older);
        r=getRightLink(older);
        setParentLink(newer, p);
        setLeftLink(newer, l);
        setRightLink(newer, r);
        if (p!=null) {
            if (getLeftLink(p)==older) setLeftLink(p,newer);
            else setRightLink(p,newer);
        } else root=newer;
        if (l!=null) setParentLink(l,newer);
        if (r!=null) setParentLink(r, newer);
    }
    
    public T peekTop() {
        return root;
    }
    
    private void siftUp(T q) {
        T p;
        while ((p=getParentLink(q))!=null && compare(q,p)<0) {
            swapWithParent(q,p);
        }
    }
    
    private void siftDown(T q) {
        T l,r;
        while (true) {
            l=getLeftLink(q);
            r=getRightLink(q);
            if (r==null) break;
            if (l!=null && compare(l,q)<0 && compare(l,r)<0) {
                //swap l and q
                swapWithParent(l,q);
            } else if (compare(r,q)<0) {
                //swap r and q
                swapWithParent(r,q);
            } else break;
        }
    }
    
    
    private T levelNextSameRow(T e) {
        T p,q;
        if (e==root) return null;
        p=getParentLink(e);
        if (getLeftLink(p)==e) return getRightLink(p);
        while ((p=levelNextSameRow(p))!=null) {
            if ((q=getLeftLink(p))!=null) return q;
            if ((q=getRightLink(p))!=null) return q;
        }
        return null;
    }
    
    private T nextRow(T e) {
        T left=root, q;
        while (e!=root) {
            e=getParentLink(e);
            left=getLeftLink(left);
        }
        while (left!=null) {
            if ((q=getLeftLink(left))!=null) return q;
            if ((q=getRightLink(left))!=null) return q;
            left=levelNextSameRow(left);
        }
        return null;
    }

    /*
        level order iterator
    */
    
    public T levelIterateNext(T e) {
        T q;
        if (e==null) return null;
        q=levelNextSameRow(e);
        if (q!=null) return q;
        if (getRightLink(e)!=null) 
            return nextRow(e);
        else
            return null;
    }
    
    public T levelIterateStart() { return root; }
    
    private void swapWithParent(T q, T p) {
        T l,r;
        T gp,brother;
        gp=getParentLink(p);
        l=getLeftLink(q);
        r=getRightLink(q);
        if (getLeftLink(p)==q) {
            brother=getRightLink(p);
            setRightLink(q,brother);
            setLeftLink(q,p);
        } else {
            brother=getLeftLink(p);
            setLeftLink(q,brother);
            setRightLink(q,p);
        }
        if (gp==null) root=q;
        else if (getLeftLink(gp)==p) setLeftLink(gp,q);
        else setRightLink(gp,q);
        setParentLink(q,gp);
        
        if (brother!=null) setParentLink(brother,q);
        setParentLink(p,q);
        setLeftLink(p,l);
        setRightLink(p,r);
        if (l!=null) setParentLink(l,p);
        if (r!=null) setParentLink(r,p);
    }
    
    //copied from IntruTreeMap
    public void printtree() {
        System.out.println("** "+count+" *********");
        printtree("",root,0,null);
        System.out.println("***************");
        System.out.println();
    }
    
    private void printtree(String lr, T q, int indent, T parent) {
        if (q==null) return;
        printtree("/",getLeftLink(q),indent+2,q);
        for (int i=0;i<indent;i++) System.out.print(" ");
        System.out.print(lr+q);
        if (parent!=getParentLink(q))
            System.out.print(" BAD PARENT");
        if (parent!=null && compare(q,parent)<0)
            System.out.print(" BAD ORDER");
        System.out.println();
        printtree("\\",getRightLink(q),indent+2,q);
    }
    
    
}
