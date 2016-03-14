/*

 (C) Paperhorse 2016
 MIT Licenced
 
*/

import com.countersort.byo_fields_intrusive_java.*;

public class Test3 {
    public static void main(String[] args) {
        testStuff();
    }
    
    static class SubTest3  {
        SubTest3 left,right,parent;
        int value;
        
        SubTest3(int v) {value=v;}
        public String toString() {return ""+value;}
        
    }
    
    static class PQManager extends IntruPriorityQueue<SubTest3> {
        //public  int compare(SubTest3 o1, SubTest3 o2) {return o1.value - o2.value;}
        public  SubTest3 getParentLink(SubTest3 e) {return e.parent;}
        public  void setParentLink(SubTest3 e,SubTest3 parent) {e.parent=parent;}
        public  SubTest3 getLeftLink(SubTest3 e) {return e.left;}
        public  void setLeftLink(SubTest3 e,SubTest3 left) {e.left=left;}
        public  SubTest3 getRightLink(SubTest3 e) {return e.right;}
        public  void setRightLink(SubTest3 e,SubTest3 right) {e.right=right;}
        @Override
        //public  int compare(SubTest3 o1, SubTest3 o2) {return o1.value - o2.value;}
        //public  int compare(SubTest3 o1, SubTest3 o2) {return new Integer(o1.value).compareTo(o2.value);}
        public int compare(SubTest3 o1, SubTest3 o2) {return o1.value>o2.value ? 1 : -1;}
    }
    
    static final int N=12;
    
    static void testStuff() {
        PQManager mgr=new PQManager();
        SubTest3[] a=new SubTest3[N];
        int i;
        for (i=0;i<N;i++) {
            a[i]=new SubTest3(i);
            mgr.insert(a[i]);
            System.out.println("Inserted "+i);
            mgr.printtree();
        }
        
        System.out.println("Level iterating");
        SubTest3 x=mgr.levelIterateStart();
        while (x!=null) {
            System.out.print(x+" ");
            x=mgr.levelIterateNext(x);
        }
        System.out.println();
        
        System.out.println("Changing Priorities");
        for (i=0;i<N;i+=3) {
            a[i].value-=100;
            mgr.changePriority(a[i]);
            mgr.printtree();
        }
        System.out.println("Deleting");
        for (i=0;i<N;i+=2) {
            a[i].value-=100;
            mgr.delete(a[i]);
            mgr.printtree();
        }
        
        System.out.println("Extracting");
        while (true) {
            x=mgr.extractTop();
            if (x==null) break;
            System.out.println("extracted "+x);
            mgr.printtree();
        }
        
    }
}
