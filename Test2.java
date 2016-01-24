
/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/


public class Test2 {
    
    static class SubTest2 implements Comparable<SubTest2> {
        SubTest2 link1, link2, link3;
        String name;
        
        public SubTest2(String nm) {name=nm;}
        public int compareTo(SubTest2 o) {
            return name.compareTo(o.name);
        }
        public String toString() {return name;}
    }
    
    public static void main(String[] args) {
        IntruTreeMap<SubTest2> hd=new IntruTreeMap<SubTest2>() {
            public  SubTest2 getParentLink(SubTest2 e) {return e.link1;}
            public  void setParentLink(SubTest2 e,SubTest2 parent) {e.link1=parent;}
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
        };
        hd.insert(new SubTest2("Roj Blake"));
        hd.insert(new SubTest2("Vila Restal"));
        hd.insert(new SubTest2("Servalan"));
        SubTest2 q=hd.iterateStart();
        while (q!=null) {
            System.out.println(q);
            q=hd.iterateNext(q);
        }
    }
    
    
}
