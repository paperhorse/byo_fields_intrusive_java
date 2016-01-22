
public abstract class IntruList<T> {
    T head;
    T tail;
    long count;

    public abstract T getNext(T e);
    public abstract void setNext(T e,T next);
    public abstract T getPrevious(T e);
    public abstract void setPrevious(T e,T previous);
    
    public void push(T e) {
        setNext(e,head);
        if (head!=null) setPrevious(head,e);
        setPrevious(e,null);
        head=e;
        count++;
    }
    
    public T pop() {
        T e;
        e=head;
        if (e!=null) {
            head=getNext(e);
            if (head!=null)
                setPrevious(head,null);
            count--;
        }
        return e;
    }
    
    
}
