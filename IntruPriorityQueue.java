/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

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
    
    public void insert(T e) {
        T p,q, tmp;
        T l,r;
        q=root;
        p=null;
        while (q!=null) {
            l=getLeftLink(q);
            r=getRightLink(q);
            if (compare(q,e)<0) {
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
        siftdown(right);
    }
    
    T removeRightMost() {
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
        System.out.println();
        printtree("\\",getRightLink(q),indent+2,q);
    }
    
    
}
