
/*

 (C) Paperhorse 2016
 MIT Licenced

Scapegoat tree based intrusive TreeMap
 
*/

package com.countersort.byo_fields_intrusive_java;

import java.util.Iterator;
import com.countersort.byo_fields_intrusive_java.IntrusiveIterator;
import com.countersort.byo_fields_intrusive_java.CompatibleIterator;


public abstract class IntruTreeMap<T> implements IntrusiveIterator<T>,
                                    Iterable<T> {

    public abstract T getParentLink(T e);
    public abstract void setParentLink(T e,T parent);
    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);
    public abstract int compare(T o1, T o2);

    long count;
    long maxcount;
    double alpha=0.6; //should be between 0.51 (balanced) and 0.99
    T root;
    
    public IntruTreeMap() {
        this(0.6);
    }
    
    public IntruTreeMap(double alpha) {
        if (alpha<=0.5) throw new RuntimeException("alpha ("+alpha+") too small. Should be 0.5 to 1.0"); 
        if (alpha>=1.0) throw new RuntimeException("alpha ("+alpha+") too large.  Should be 0.5 to 1.0"); 
        this.alpha=alpha;
    }
    
    public void clear() {
        root=null;
        count=maxcount=0;
    }
    /*
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
        //System.out.printf("insert n=%.2f\n",n);
        if (n<1.0) {
            T r;
            long[] cc=new long[1];
            cc[0]=1;
            r=findRebalance(e,cc);
            //System.out.println("rebalance="+r);
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
    */
    
    private static int INSERT=2;
    private static int FIND=1; 

    private T findInserter(T e, int findInsert) {
        int c=0;
        T q=root,p=null;
        double n=(double) (1+count);
        while (q!=null) {
            c=compare(q,e);
            if (c==0 && (findInsert & FIND)!=0) return q;
            p=q;
            if (c>0) q=getLeftLink(q);
            else q=getRightLink(q);
            n=n*alpha;
        }
        if ((findInsert & INSERT)==0) return null;
        setLeftLink(e,null);
        setRightLink(e,null);
        count++;
        if (count>maxcount) maxcount=count;
        setParentLink(e,p);
        if (p==null) {
            root=e;
            return e;
        }
        if (c>0) setLeftLink(p,e);
        else setRightLink(p,e);
        //System.out.printf("insert n=%.2f\n",n);
        if (n<1.0) {
            T r;
            long[] cc=new long[1];
            cc[0]=1;
            r=findRebalance(e,cc);
            //System.out.println("rebalance="+r);
            if (r==null) return e; //shouldnt happen
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
        return e;
    }


    
    public void insert(T e) {
        T ignore=findInserter(e, INSERT);
    }
    
    public T find(T key) {
        return findInserter(key, FIND);
    }
    
    public T findOrInsert(T e) {
        return findInserter(e, FIND+INSERT);
    }

    public static final int LT=1;
    public static final int EQ=2;
    public static final int GT=4;
    
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
        if ((lt_eq_gt & GT)!=0) {
            T gt2=null;
            if (eq!=null) gt2=leftmost(getRightLink(eq));
            if (gt2!=null) gt=gt2;
            if (gt!=null) return gt;
        }
        if ((lt_eq_gt & LT)!=0) {
            T lt2=null;
            if (eq!=null) lt2=rightmost(getLeftLink(eq));
            if (lt2!=null) lt=lt2;
            if (lt!=null) return lt;
        }
        return null;
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
    
    @Override
    public T iterateStart() {
        return leftmost(root);
    }
    
    @Override
    public T iterateLast() {
        return rightmost(root);
    }
    
    @Override
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

    @Override
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
    
    @Override
    public Iterator<T> iterator() {
        return new CompatibleIterator<T>(this);
    }
    
    public void delete(T e) {
        T l,r,p,d,q;
        //boolean dbg=false;
        //if ((""+e).equals("13")) dbg=true;
        d=e;
        if (getLeftLink(d)==null) {
            //if (dbg) System.out.println("left null - graftup right");
            r=getRightLink(d);
            p=getParentLink(d);
            if (r!=null) setParentLink(r,p);
            if (p==null) root=r;
            else if (getLeftLink(p)==d) setLeftLink(p,r);
            else setRightLink(p,r);
        } else { 
            //if (dbg) System.out.println("left not null - descend right then left");            
            if (getRightLink(d)!=null) {
                q=getLeftLink(d);
                while (q!=null) {
                    d=q;
                    q=getRightLink(q);
                }            
            }
            p=getParentLink(d);
            l=getLeftLink(d);
            //if (dbg) System.out.println("found delete node="+d+" par="+p+"left="+l);
        
            if (p==null) root=l;
            else if (getLeftLink(p)==d) setLeftLink(p,l);
            else setRightLink(p,l);
            if (l!=null) setParentLink(l,p);
            if (d!=e) {
                //replace w with d
                //if (dbg) System.out.println("replacing internal node");
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
        h=1;
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
        h=1;
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
