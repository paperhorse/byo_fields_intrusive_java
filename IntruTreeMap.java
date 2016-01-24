
/*

 (C) Paperhorse 2016
 MIT Licenced

Scapegoat tree based intrusive TreeMap
 
*/


public abstract class IntruTreeMap<T extends Comparable<T> > {

    public abstract T getParentLink(T e);
    public abstract void setParentLink(T e,T parent);
    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    
    long count;
    double alpha=0.6;
    T root;
    
    public T find(T key) {
        int c;
        T q=root;
        while (q!=null) {
            c=q.compareTo(key);
            if (c==0) return q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
        }
        return null;
    }
    
    public void insert(T e) {
        int c=0;
        T q=root,p=null;
        double n=(double) (1+count);
        setLeftLink(e,null);
        setRightLink(e,null);
        while (q!=null) {
            c=q.compareTo(e);
            //if (c==0) return q;
            p=q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
            n=n*alpha;
        }
        count++;
        setParentLink(e,p);
        if (p==null) {
            root=e;
            return;
        }
        if (c>0) setLeftLink(p,e);
        else setRightLink(p,e);
        if (n<1.0) {
            T r;
            long[] cc=new long[1];
            cc[0]=1;
            r=findRebalance(e,cc);
            if (r==null) return;
            p=getParentLink(r);
            boolean isLeft=false;
            if (p!=null && getLeftLink(p)==r) isLeft=true;
            q=rebalance(r,cc[0]);
            setParentLink(q,p);
            if (p==null) root=q;
            else if (isLeft)
                setLeftLink(p,q);
            else
                setRightLink(p,q);
        }
    }
    
    private T findRebalance(T q, long[] cc) {
        long c=cc[0];
        long otherCount;
        T p,other;
        while (true) {
            p=getParentLink(q);
            if (p==null) return null;
            if (getLeftLink(p)==q) other=getRightLink(p);
            else other=getLeftLink(p);
            otherCount=countTree(other);
            if ((otherCount+c+1)*alpha < (double) c)
                break;
            c=otherCount+c+1;
            q=p;
        }
        c=otherCount+c+1;
        cc[0]=c;
        return p;
    }
    
    private long countTree(T q) {
        long c=0;
        while (q!=null) {
            c+=1+countTree(getRightLink(q));
            q=getLeftLink(q);
        }
        return c;
    }
    
    
    private T bal;
    
    private T rebalance(T q, long c) {
        setParentLink(q,null);
        bal=leftmost(q);
        return rebalance(c);
    }
    
    private T leftmost(T q) {
        T p=null;
        while (q!=null) {
            p=q;
            q=getLeftLink(q);
        }
        return p;
    }

    private T rightmost(T q) {
        T p=null;
        while (q!=null) {
            p=q;
            q=getRightLink(q);
        }
        return p;
    }

    
    private T rebalance(long c) {
        T l,r,rr,q,p;
        if (c<=0) return null;
        c--;
        l=rebalance(c/2);
        q=bal;
        p=getParentLink(q);
        rr=getRightLink(q);
        if (rr!=null) {
            setParentLink(rr,p);
            bal=leftmost(rr);
        } else bal=p;
        r=rebalance(c-c/2);
        setLeftLink(q,l);
        setRightLink(q,r);
        if (l!=null) setParentLink(l,q);
        if (r!=null) setParentLink(r,q);
        return q;
    }
    
    public T iterateStart() {
        return leftmost(root);
    }
    
    public T iterateNext(T e) {
        T r,p;
        r=getRightLink(e);
        if (r!=null) return leftmost(r);
        while (true) {
            p=getParentLink(e);
            if (p==null) return null;
            if (getLeftLink(p)==e) return p;
            e=p;
        }
    }

    public T iteratePrevious(T e) {
        T r,p;
        r=getLeftLink(e);
        if (r!=null) return rightmost(r);
        while (true) {
            p=getParentLink(e);
            if (p==null) return null;
            if (getRightLink(p)==e) return p;
            e=p;
        }
    }
    
    
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
        System.out.println();
        printtree("\\",getRightLink(q),indent+2,q);
    }
}
