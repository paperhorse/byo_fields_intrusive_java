/*
 (C) Paperhorse 2016
 MIT Licenced
 
 Intrusive Double Linked List
 
*/
import java.util.Comparator;


public abstract class IntruList<T> {
    T head;
    T tail;
    long count;

    public abstract T getNextLink(T e);
    public abstract void setNextLink(T e,T next);
    public abstract T getPreviousLink(T e);
    public abstract void setPreviousLink(T e,T previous);
    
    //was push
    public void prepend(T e) {
        setNextLink(e,head);
        if (head!=null) setPreviousLink(head,e);
        else tail=e;
        setPreviousLink(e,null);
        head=e;
        count++;
    }
    
    //was pop
    public T extractFirst() {
        T e;
        e=head;
        if (e!=null) {
            head=getNextLink(e);
            if (head!=null)
                setPreviousLink(head,null);
            else
                tail=null;
            count--;
        }
        return e;
    }
    
    public void insertAfter(T existingElement, T newElement) {
        T n=getNextLink(existingElement);
        setNextLink(existingElement, newElement);
        setPreviousLink(newElement, existingElement);
        if (n!=null) {
            setNextLink(newElement, n);
            setPreviousLink(n, newElement);
        } else {
            tail=newElement;
            setNextLink(newElement, null);
        }
        count++;
    }
    
    public void append(T e) {
        setPreviousLink(e, tail);
        setNextLink(tail, e);
        setNextLink(e, null);
        if (tail==null) head=e;
        tail=e;
        count++;
    }
    
    public T extractLast() {
        T p,q;
        if (tail==null) return null;
        count--;
        q=tail;
        p=getPreviousLink(q);
        if (p!=null) {
            setNextLink(p, null);
            tail=p;
        } else {
            tail=head=null;
        }
        return q;
    }
    
    public void delete(T e) {
        T p,n;
        count--;
        p=getPreviousLink(e);
        n=getNextLink(e);
        if (p!=null) setNextLink(p,n);
        else head=n;
        if (n!=null) setPreviousLink(n,p);
        else tail=p;
    }
    
    public long length() {return count;}
    
    private T doubleLink(T q) {
        T p=null;
        while (q!=null) {
            setPreviousLink(q,p);
            p=q;
            q=getNextLink(q);
        }
        return p;
    }
    
    private T merge(T a, T b, Comparator<T> cmp) {
        T hd=null, tl=null;
        while (a!=null && b!=null) {
            if (cmp.compare(a,b)<=0) {
                if (tl!=null) setNextLink(tl, a);
                else hd=a;
                tl=a;
                a=getNextLink(a); 
            } else {
                if (tl!=null) setNextLink(tl, b);
                else hd=b;
                tl=b;
                b=getNextLink(b); 
            }
        }
        if (a==null) a=b;
        if (tl==null) return a;
        else setNextLink(tl,a);
        return hd;
    }
    
    private T sort(long cnt,Comparator<T> cmp) {
        T a, b;
        if (cnt<=0) return null;
        if (cnt==1) {
            a=head;
            head=getNextLink(a);
            setNextLink(a,null);
            return a;
        } else {
            a=sort(cnt/2,cmp);
            b=sort(cnt-cnt/2,cmp);
            return merge(a,b,cmp);
        }
    }
    
    public void sort(Comparator<T> cmp) {
        head=sort(count, cmp);
        tail=doubleLink(head);
    }
    
    public T iterateNext(T e) {
        return getNextLink(e);
    }
    
    public T iteratePrevious(T e) {
        return getPreviousLink(e);
    }
    
    public T iterateStart() {
        return head;
    }

    public T iterateLast() {
        return tail;
    }

    
    public void printList() {
        T q,p;
        int c=0;
        p=null;
        q=head;
        String comma="";
        while (q!=null) {
            System.out.print(comma+q);
            comma=",";
            if (p!=getPreviousLink(q))
                System.out.print("(PrevBad)");
            p=q;
            q=getNextLink(q);
            c++;
        }
        if (c!=count) {
            System.out.print(" Bad count="+count+" should be "+c);
        }
        System.out.println();
    }
    
}
