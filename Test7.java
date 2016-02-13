/*
 (C) Paperhorse 2016
 MIT Licenced
 
*/

import com.countersort.byo_fields_intrusive_java.*;


public class Test7 {
    static class NodeMap extends IntruReallyTreeMap<String,Node> {
        public Node getParentLink(Node e) {return e.parent;}
        public void setParentLink(Node e,Node parent) {e.parent=parent;}
        public Node getLeftLink(Node e) {return e.left;}
        public void setLeftLink(Node e,Node left) {e.left=left;}
        public Node getRightLink(Node e) {return e.right;}
        public void setRightLink(Node e,Node right) {e.right=right;}
        public String getKey(Node e) {return e.name;};            
    }
    static class Node {
        Node left, right, parent;
        String name;
        int age;
        
        Node (String name, int age) {
            this.name=name; this.age=age;
        }
        public String toString(){return name+":"+age;}
    }
    
    public static void main(String[] args) {
        NodeMap map=new NodeMap();
        map.insert(new Node("Doctor", 2000000000));
        map.insert(new Node("Cap Jack",5000));
        map.insert(new Node("Romana",120));
        map.insert(new Node("Rose",19));
        System.out.println(map.find("Doctor"));
        System.out.println(map.find("Rose"));
        System.out.println(map.find("Davros"));
    }
}
