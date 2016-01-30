
/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/


public class Test2 {
    
    
    public static void main(String[] args) {
        //System.out.printf("%02d\n",5);
        //quicktest();
        hardtest();
    }

    static class SubTest2 /* implements Comparable<SubTest2> */ {
        SubTest2 link1, link2, link3;
        String name;
        
        public SubTest2(String nm) {name=nm;}
        /*public int compareTo(SubTest2 o) {
            return name.compareTo(o.name);
        }*/
        public String toString() {return name;}
    }

    
    static void quicktest() {
        IntruTreeMap<SubTest2> hd=new IntruTreeMap<SubTest2>() {
            public  SubTest2 getParentLink(SubTest2 e) {return e.link1;}
            public  void setParentLink(SubTest2 e,SubTest2 parent) {e.link1=parent;}
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
            public int compare(SubTest2 o1, SubTest2 o2) {return o1.name.compareTo(o2.name);}
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
    
    static void hardtest() {
        int i;
        IntruTreeMap<SubTest2> hd=new IntruTreeMap<SubTest2>() {
            public  SubTest2 getParentLink(SubTest2 e) {return e.link1;}
            public  void setParentLink(SubTest2 e,SubTest2 parent) {e.link1=parent;}
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
            public int compare(SubTest2 o1, SubTest2 o2) {return o1.name.compareTo(o2.name);}
        };
        SubTest2[] a=new SubTest2[16];
        for (i=0;i<16;i++) {
            System.out.println("INSERT "+i);
            hd.insert(a[i]=new SubTest2("".format("%02d",i)));
            hd.printtree();
        }
        
        SubTest2 q;
        System.out.println("\nIteration Test");
        for (q=hd.iterateStart();q!=null;q=hd.iterateNext(q))
            System.out.print(" "+q);
        System.out.println();
        System.out.println("\nReverse Iteration Test");
        for (q=hd.iterateEnd();q!=null;q=hd.iteratePrevious(q))
            System.out.print(" "+q);
        System.out.println();
        int x=0;
        for (i=0;i<16;i++) {
            x=(5*x+3) & 15;
            System.out.println("Delete "+x);
            hd.delete(a[x]);
            hd.printtree();
        }
        System.out.println();
    
    }
    
    
}
