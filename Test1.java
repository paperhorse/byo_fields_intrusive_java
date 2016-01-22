

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
            public  SubTest getNext(SubTest e) {return e.next;}
            public  void setNext(SubTest e,SubTest next) {e.next=next;}
            public  SubTest getPrevious(SubTest e) {return e.previous;}
            public  void setPrevious(SubTest e,SubTest previous) 
                            {e.previous=previous;}
        };
        hd.push(new SubTest("Olivia Dunham"));
        hd.push(new SubTest("Astrid Farnsworth"));
        hd.push(new SubTest("Peter Bishop"));
        hd.push(new SubTest("Walter Bishop"));
        SubTest s;
        while ((s=(SubTest)hd.pop())!=null) System.out.println(s);
    }
    
}
