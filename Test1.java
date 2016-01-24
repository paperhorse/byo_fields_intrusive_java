/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

import java.util.Comparator;

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
        hd.push(new SubTest("Olivia Dunham"));
        hd.push(new SubTest("Astrid Farnsworth"));
        hd.push(new SubTest("Peter Bishop"));
        hd.push(new SubTest("Walter Bishop"));
        hd.sort(new Comparator<SubTest>() {
            public int compare(SubTest o1,SubTest o2) {
                return o1.customer.compareTo(o2.customer);
            }
            public boolean equals(SubTest o1, SubTest o2) {
                return o1.customer.equals(o2.customer);
            }
        });
        SubTest s;
        while ((s=(SubTest)hd.pop())!=null) System.out.println(s);
    }
    
}
