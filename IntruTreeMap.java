
/*

 (C) Paperhorse 2016
 MIT Licenced

Scapegoat tree based intrusive TreeMap
 
*/


public abstract class IntruTreeMap<T> {

    public abstract T getParentLink(T e);
    public abstract void setParentLink(T e,T parent);
    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    public abstract int compare(T o1, T o2);

    long count;
    long maxcount;
    double alpha=0.6;
    T root;
    
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
        int c=0;
        T q=root,p=null;
        double n=(double) (1+count);
        setLeftLink(e,null);
        setRightLink(e,null);
        while (q!=null) {
            c=compare(q,e);
            //if (c==0) return q;
            p=q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
            n=n*alpha;
        }
        count++;
        if (count>maxcount) maxcount=count;
        setParentLink(e,p);
        if (p==null) {
            root=e;
            return;
        }
        if (c>0) setLeftLink(p,e);
        else setRightLink(p,e);
        System.out.printf("insert n=%.2f\n",n);
        if (n<1.0) {
            T r;
            long[] cc=new long[1];
            cc[0]=1;
            r=findRebalance(e,cc);
            System.out.println("rebalance="+r);
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
    
    public T iterateEnd() {
        return rightmost(root);
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
    
    public void delete(T e) {
        T l,r,p,d,q;
        boolean dbg=false;
        if ((""+e).equals("13")) dbg=true;
        d=e;
        if (getLeftLink(d)==null) {
            if (dbg) System.out.println("left null - graftup right");
            r=getRightLink(d);
            p=getParentLink(d);
            if (r!=null) setParentLink(r,p);
            if (p==null) root=r;
            else if (getLeftLink(p)==d) setLeftLink(p,r);
            else setRightLink(p,r);
        } else { 
            if (dbg) System.out.println("left not null - descend right then left");            
            if (getRightLink(d)!=null) {
                q=getLeftLink(d);
                while (q!=null) {
                    d=q;
                    q=getRightLink(q);
                }            
            }
            p=getParentLink(d);
            l=getLeftLink(d);
            if (dbg) System.out.println("found delete node="+d+" par="+p+"left="+l);
        
            if (p==null) root=l;
            else if (getLeftLink(p)==d) setLeftLink(p,l);
            else setRightLink(p,l);
            if (l!=null) setParentLink(l,p);
            if (d!=e) {
                //replace w with d
                if (dbg) System.out.println("replacing internal node");
                p=getParentLink(e);
                l=getLeftLink(e);
                r=getRightLink(e);
                setParentLink(d,p);
                setLeftLink(d,l);
                setRightLink(d,r);
                if (l!=null) setParentLink(l,d);
                if (r!=null) setParentLink(r,d);
                if (p==null) root=d;
                else if (getLeftLink(p)==e) setLeftLink(p,d);
                else setRightLink(p,d);
            }
        }
        count--;
        if (1.0*count<alpha*maxcount) {
            if (count>0) {
                root=rebalance(root,count);
                setParentLink(root,null);
            }
            maxcount=count;
        }
    }
    
    private int CalcTheoryMaxHt() {
        double c;
        int h;
        c=count;
        h=0;
        while (c>=1.0) {
            c*=alpha;
            h++;
        }
        return h-1;
    }
    private int CalcTheoryMinHt() {
        double c;
        int h;
        c=count;
        h=0;
        while (c>1.0) {
            c=c-1.0-c*alpha;
            h++;
        }
        return h-1;
    }
    private int calcMaxHt(T q) {
        if (q==null) return 0;
        return 1+Math.max(calcMaxHt(getLeftLink(q)),
                        calcMaxHt(getRightLink(q)));
    }

    private int calcMinHt(T q) {
        if (q==null) return 0;
        return 1+Math.min(calcMaxHt(getLeftLink(q)),
                        calcMaxHt(getRightLink(q)));
    }
    
    public void printtree() {
        int maxht, minht, theory_maxht, theory_minht;
        System.out.println("** "+count+" *********");
        maxht=calcMaxHt(root);
        minht=calcMinHt(root);
        theory_maxht=CalcTheoryMaxHt();
        theory_minht=CalcTheoryMinHt();
        System.out.printf("Max Ht %d/%d  Min Ht %d/%d\n",
            maxht, theory_maxht, minht, theory_minht);
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
