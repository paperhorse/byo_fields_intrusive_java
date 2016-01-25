
public abstract class IntruPriorityQueue<T extends Comparable<T> > {

    public abstract T getParentLink(T e);
    public abstract void setParentLink(T e,T parent);
    public abstract T getLeftLink(T e);
    public abstract void setLeftLink(T e,T left);
    public abstract T getRightLink(T e);
    public abstract void setRightLink(T e,T right);

    T root;
    
    public void add(T e) {
        T p,q, tmp;
        T l,r;
        q=root;
        p=null;
        while (q!=null) {
            l=getLeftLink(q);
            r=getRightLink(q);
            if (q.compareTo(e)<0) {
                //replacing q with e
                setParentLink(e,p);
                if (p==null) root=e;
                else setRightLink(p,e);
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
    }
}
