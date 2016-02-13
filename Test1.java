/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

import java.util.Comparator;
import com.countersort.byo_fields_intrusive_java.*;

public class Test1 {
    
    static class SubTest {
        String customer;
        SubTest next;
        SubTest previous;
        
        public SubTest(String c) {
            customer=c;
        }
        
        @Override
        public String toString() {
            return customer;
        }
        
    }
    
    public static void main(String[] args) {
        IntruList<SubTest> hd=new IntruList<SubTest>() {
            public  SubTest getNextLink(SubTest e) {return e.next;}
            public  void setNextLink(SubTest e,SubTest next) {e.next=next;}
            public  SubTest getPreviousLink(SubTest e) {return e.previous;}
            public  void setPreviousLink(SubTest e,SubTest previous) 
                            {e.previous=previous;}
        };
        SubTest d;
        hd.prepend(new SubTest("Olivia Dunham"));
        hd.prepend(new SubTest("Astrid Farnsworth"));
        hd.prepend((d=new SubTest("Peter Bishop")));
        hd.prepend(new SubTest("Walter Bishop"));
        hd.prepend(new SubTest("Broils"));
        hd.prepend(new SubTest("Nina Sharpe"));
        /*
        hd.sort(new Comparator<SubTest>() {
            public int compare(SubTest o1,SubTest o2) {
                return o1.customer.compareTo(o2.customer);
            }
            public boolean equals(SubTest o1, SubTest o2) {
                return o1.customer.equals(o2.customer);
            }
        });*/
        SubTest s;
        hd.printList();
        hd.delete(d);
        hd.printList();
        while ((s=(SubTest)hd.extractFirst())!=null) System.out.println(s);
        hd.printList();
    }
    
}
