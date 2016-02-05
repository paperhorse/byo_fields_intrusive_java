
/*

 (C) Paperhorse 2016
 MIT Licenced

Scapegoat tree based intrusive TreeMap
Lightweight doesn't have parent links
 
*/

public abstract class IntruTreeLite<T> {

    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    public abstract int compare(T o1, T o2);

    long count;
    long maxcount;
    double alpha=0.6;
    T root;
    
    final boolean FALSE=false;
    
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
    
    public void insert(T e) {
        System.out.println("INSERT "+e);
        long oldcount=++count;
        setLeftLink(e, null);
        setRightLink(e, null);
        count=0; //count now used as subtree count when search for scapegoat tree
        root=insert(root,e,1.0*oldcount);
        count=oldcount;
    }

    T insert(T q, T e, double alphacount) {
        T r=insertX(q,e,alphacount);
        System.out.println("insert returns");
        printTree(r);
        System.out.println();
        return r;
    }
    
    public T insertX(T q, T e, double alphacount) {
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
