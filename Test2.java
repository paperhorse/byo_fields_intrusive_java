
/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

import com.countersort.byo_fields_intrusive_java.*;

public class Test2 {
    
    static int N=100;
    
    public static void main(String[] args) {
        //System.out.printf("%02d\n",5);
        String arg="";
        if (args.length>=1) arg=args[0];
        if (arg.startsWith("Q")) quicktest();
        else {
            try {
                N=Integer.parseInt(arg);
            } catch(Exception e){}
            hardtest();
        }
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
        IntruTreeMap<SubTest2> tree=new IntruTreeMap<SubTest2>() {
            public  SubTest2 getParentLink(SubTest2 e) {return e.link1;}
            public  void setParentLink(SubTest2 e,SubTest2 parent) {e.link1=parent;}
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
            public int compare(SubTest2 o1, SubTest2 o2) {return o1.name.compareTo(o2.name);}
        };
        tree.insert(new SubTest2("Roj Blake"));
        tree.insert(new SubTest2("Vila Restal"));
        tree.insert(new SubTest2("Servalan"));
        tree.insert(new SubTest2("Jenna"));
        tree.insert(new SubTest2("Cally"));
        tree.insert(new SubTest2("Gan"));
        
        System.out.println("Finding");
        System.out.println(tree.find(new SubTest2("Jenna")));
        System.out.println(tree.find(new SubTest2("Avon")));
        
        System.out.println("Iterating");
        SubTest2 q=tree.iterateStart();
        while (q!=null) {
            System.out.println(q);
            q=tree.iterateNext(q);
        }
    }
    
    static void hardtest() {
        int i;
        IntruTreeMap<SubTest2> tree=new IntruTreeMap<SubTest2>() {
            public  SubTest2 getParentLink(SubTest2 e) {return e.link1;}
            public  void setParentLink(SubTest2 e,SubTest2 parent) {e.link1=parent;}
            public  SubTest2 getLeftLink(SubTest2 e) {return e.link2;}
            public  void setLeftLink(SubTest2 e,SubTest2 left) {e.link2=left;}
            public  SubTest2 getRightLink(SubTest2 e) {return e.link3;}
            public  void setRightLink(SubTest2 e,SubTest2 right) {e.link3=right;}
            public int compare(SubTest2 o1, SubTest2 o2) {return o1.name.compareTo(o2.name);}
        };
        SubTest2[] a=new SubTest2[N];
        for (i=0;i<N;i++) {
            System.out.println("INSERT "+i);
            tree.insert(a[i]=new SubTest2("".format("%02d",i)));
            tree.printtree();
        }
        
        
        SubTest2 q;
        System.out.println("\nIteration Test");
        for (q=tree.iterateStart();q!=null;q=tree.iterateNext(q))
            System.out.print(" "+q);
        System.out.println();
        System.out.println("\nReverse Iteration Test");
        for (q=tree.iterateEnd();q!=null;q=tree.iteratePrevious(q))
            System.out.print(" "+q);
        System.out.println();
        int x=0;
        for (i=0;i<N;i++) {
            do {
                x=(0x95*x+0x71) & 255;
            } while (x>=N);
            System.out.println("Delete "+x);
            tree.delete(a[x]);
            tree.printtree();
        }
        System.out.println();
        
    }
    
    
}
