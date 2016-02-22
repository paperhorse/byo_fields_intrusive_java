
/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

import com.countersort.byo_fields_intrusive_java.*;

public class Test6 {
    
    static final int N=100;
    
    public static void main(String[] args) {
        //System.out.printf("%02d\n",5);
        quicktest();
        //hardtest();
    }

    static class SubTest6  {
        SubTest6 left, right;
        String name;
        
        public SubTest6(String nm) {name=nm;}
        public String toString() {return name;}
    }

    
    static void quicktest() {
        IntruTreeLite<SubTest6> hd=new IntruTreeLite<SubTest6>() {
            public  SubTest6 getLeftLink(SubTest6 e) {return e.left;}
            public  void setLeftLink(SubTest6 e,SubTest6 left) {e.left=left;}
            public  SubTest6 getRightLink(SubTest6 e) {return e.right;}
            public  void setRightLink(SubTest6 e,SubTest6 right) {e.right=right;}
            public int compare(SubTest6 o1, SubTest6 o2) {return o1.name.compareTo(o2.name);}
        };
        hd.insert(new SubTest6("GG Roj Blake"));
        hd.insert(new SubTest6("FF Vila Restal"));
        hd.insert(new SubTest6("EE Servalan"));
        hd.insert(new SubTest6("DD Kerr Avon"));
        hd.insert(new SubTest6("CC Zen"));
        hd.insert(new SubTest6("BB Cally"));
        hd.insert(new SubTest6("AA Soolin"));
        hd.visitRange(new IntruTreeLite.Visitor<SubTest6>() {
            public void visit(SubTest6 e) {
                String n=e.name;
                /* int p=n.lastIndexOf(' ');
                if (p>=0) n=n.substring(p+1); */
                System.out.println("Visiting "+n);
            }
        },new SubTest6("BB"), new SubTest6("EEZ"));
        hd.printTree();
        
        System.out.println("Find lt eq gt test");
        SubTest6 qq=new SubTest6("CC Zen");
        System.out.println("Find = "+hd.find_lt_eq_gt(qq,IntruTreeLite.EQ));
        System.out.println("Find < "+hd.find_lt_eq_gt(qq,IntruTreeLite.LT));
        System.out.println("Find > "+hd.find_lt_eq_gt(qq,IntruTreeLite.GT));
        System.out.println("Find <= "+hd.find_lt_eq_gt(qq,IntruTreeLite.EQ+IntruTreeLite.LT));
        System.out.println("Find >= "+hd.find_lt_eq_gt(qq,IntruTreeLite.EQ+IntruTreeLite.GT));
        System.out.println("Find <> "+hd.find_lt_eq_gt(qq,IntruTreeLite.LT+IntruTreeLite.GT));
        
        
        System.out.println("iteration test");
        SubTest6 q=hd.iterateStart();
        while (q!=null) {
            System.out.println(q);
            q=hd.iterateNext(q);
        }
        
        System.out.println("reverse iteration test");
        q=hd.iterateLast();
        while (q!=null) {
            System.out.println(q);
            q=hd.iteratePrevious(q);
        }
    }
    
    /*
    static void hardtest() {
        int i;
        IntruTreeLite<SubTest2> hd=new IntruTreeLite<SubTest2>() {
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
            public int compare(SubTest2 o1, SubTest2 o2) {return o1.name.compareTo(o2.name);}
        };
        SubTest2[] a=new SubTest2[N];
        for (i=0;i<N;i++) {
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
        for (i=0;i<N;i++) {
            do {
                x=(0x95*x+0x71) & 255;
            } while (x>=N);
            System.out.println("Delete "+x);
            hd.delete(a[x]);
            hd.printtree();
        }
        System.out.println();
        
    }
    */
    
}
