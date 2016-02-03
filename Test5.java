
import java.util.Arrays;

public class Test5 {
    public static void main(String[] args) {
        new Test5().main2();
    }
    
    void main2() {
        //findPrimes();
        testHash();
    }
    
    static class Node {
        Node next;
        String name;
        int count;
        
        Node(String nm) {name=nm;count=1;}
        public String toString() {return name;}
    }
    
    static class Hash extends IntruHashMap<Node> {
        public  Node getNextLink(Node e) {return e.next;}
        public  void setNextLink(Node e,Node next) {e.next=next;}
        
        public  boolean equality(Node o1, Node o2) {return o1.name.equals(o2.name);}
        public  int hashCoder(Node o1) {return o1.name.hashCode();}
        
    }
    
    Hash hash=new Hash();
    
    void add(String nm) {
        Node node=new Node(nm);
        Node f;
        if ((f=hash.find(node))!=null) f.count++;
        else hash.insert(node); 
    }
    
    void testHash() {
        int i;
        add("kirk");
        add("spock");
        add("kirk");
        add("checkov");
        add("sulu");
        add("kirk");
        //hash.delete(hash.find(new Node("spock")));
        //for (i=0;i<200;i++) add("tribble"+i);
        Node q=hash.iteratorStart();
        while (q!=null) {
            System.out.println(q.name+" "+q.count);
            q=hash.iteratorNext(q);
        }
    }
    
    boolean isPrime(int n) {
        int d;
        if (n % 2==0) return n==2;
        for (d=3;d*d<=n;d+=2) {
            if (n % d==0) return false;
        }
        return true;
    }
    
    void findPrimes() {
        double r;
        double r10;
        int rr=0;
        int i;
        int[] primes=new int[20];
        primes[0]=17;
        for (i=1;i<primes.length;i++) {
            //do {
                r10=primes[i-1]*(3.0+3.0*Math.random());
                //r10=Math.pow(10.0,r);
                if (r10>32768.0*65536.0-1.0) break;
                rr=(int) r10;
                while  (!isPrime(rr)) rr+=1;
                //break;
            //} while (true);
            primes[i]=rr;
        }
        Arrays.sort(primes);
        for (int j : primes) System.out.print(" "+j);
        System.out.println();
    }
    
}
