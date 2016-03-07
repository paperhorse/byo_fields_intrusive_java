/*
    Test for SList
    
    (c) 2016 by Paperhorse
    MIT licensed
*/
import com.countersort.byo_fields_intrusive_java.*;
import java.util.Iterator;

public class Test4 {
    public static void main(String[] args) {
        new Test4().main2();
    }
    
    static class Node {
        Node next;
        String name;
        
        Node(String n) {name=n;}
        
        @Override
        public String toString() {return name;}
    }
    
    static class SList extends IntruSList<Node> {
        public Node getNextLink(Node e) {return e.next;}
        public void setNextLink(Node e, Node next) {e.next=next;} 
    }
    
    
    void main2() {
        SList slist=new SList();
        slist.append(new Node("penny"));
        slist.append(new Node("robot B9"));
        slist.prepend(new Node("will"));
        slist.append(new Node("professor john"));
        slist.append(new Node("don"));
        slist.append(new Node("dr smith"));
        slist.prepend(new Node("judy"));
        slist.append(new Node("maureen"));
        for (Node n : slist) {
            System.out.println(n);
        }
        System.out.println();
        Iterator<Node> i=slist.iterator();
        while (i.hasNext()) {
            Node n2=i.next();
            if (n2.name.charAt(0)>='p') {
                System.out.println("deleting "+n2);
                i.remove();
            }
        }
        for (Node n3 : slist) {
            System.out.println(n3);
        }
        
    }
}
