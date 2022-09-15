package week1;

import java.util.*;

public class Day2 {

}
/**
 *  1. Exceptions
 *
 *                Throwable
 *      /                             \
 *     error (exit program)          Exception
 *     stackOverFlow..              checked(compile)     unchecked(run)
 */

class Test21 {
//    static void dummyMethod() {
//        try{
//            System.out.println(Class.forName("Day2"));
//        }catch (ClassNotFoundException ex) {
//            System.out.println("ex happen");
//        }catch(ArrayIndexOutOfBoundsException ex){
//            System.out.println("another ex");
//        }catch(Exception ex){
//            System.out.println("ex");
//        }finally {
//            //no matter ex happens or not
//            System.out.println("this is finally");
//        }
//
//    }
    static void dummy(){
        int[] arr = new int[1];
//        try{
//            System.out.println(arr[1]);
//        }catch (Exception ex) {
//            System.out.println(ex);
//        }
        System.out.println(arr[1]);


    }
    public static void main(String[] args) {
//        dummyMethod();
        try{
            dummy();
        }catch (Exception ex) {
            System.out.println("the message: "+ex);
        }

    }
}

/**
 * How to resolve checked exception
 *
 *      1.throws (checked)
 *      2. try/  catch/ finally
 */

/**
 * 2. Array
 *      type[]
 *      fixed size memory block
 *      memory allocation is contiguous -> access O(1)
 *
 *      primitive type  vs object type
 */
class Test22 {
    public static void main(String[] args) {
//        int[] arr =new int[5];
//        //arr[0] arr[1] arr[2] ...
//        for(int e: arr) {
//            System.out.println(e);
//        }
        Integer[] arr = new Integer[5];
//        int i = arr[0];
        for(int i=0;i<arr.length;i++) {
            arr[i]=0;
        }
        //int i= arr[0];
        for(Integer e: arr) {
            System.out.println(e);
        }

    }
}

/**
 * 3. List
 *  LinkedList node(pointer prev, next)
 *      addLast O(1)
 *      add(index, item) O(n) traverse O(1) manipulate pointer
 *      disad
 *      get() O(n) :traverse through first/last O(n) next...next prev.prev
 *  ArrayList Object[]
 *      change size: exceed size -> grow O(n)
 *      get() O(1)
 */
class Test23 {
    //reference value ->address ->obj
    //list[addr1->obj(list)]

    //variable store addr(reference value)
    public static void main(String[] args) {
        Integer i1=1;
        Integer i2=i1;
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(0,10);
        list.get(1);
        list = new ArrayList<>();
        int i=2;

        System.out.println(i);


    }
}
/**
 *  4. HashMap
 *      Key Value
 *      Node[]
 *      len 4
 *      8
 *      Hash Collision
 *      O(1) retrieve time complexity
 *      [Node(key, value, next)] [kv1] [kv2] [kv3]       hash==3 %4==3
 *                                     hash =7%4==3
 *       kv5
 *
 *       get()
 *       put()
 *       Key.hashcode() -> rehash(hc) -> rehash value% length of node array (n-1)&hash == hash%n-> index find the target bucket -> hash collison
 *
 *       HashMap
 *          Node (key, value, next)
 *          put -> putVal ->(hash(key), key, value)
 *                  1.check whether tab is empty
 *                  2. if there no collison , we just create new node and put into the bucket
 *                  3. if the first node in the target bucket is the key that find, just change the value of the existed Node
 *                  4. further if it is treenode, use method of tree
 *                  5. traverse the linkedlist using the "next" pointer, to see if there is a pre exist node for this key
 *                      if we traverse all the node, and cannot find a hit, we gonna create a newNode and append it at then end
 *                  6. if find a hit, we break the loop, e will hold the pre exist node, and just change the pre exist node's value field
 *
 */

class TestHashMap{
    class Node{}
    Node[] table;
    void put(){
        Node[] tab;
        tab = this.table;
    }

    public static void main(String[] args) {
        HashMap<Student, Integer> hm = new HashMap<>();
        Student stu1 = new Student(1,"xx");
//        Student stu2 = new Student(1,"xx");
//        System.out.println(stu1==stu2);
//        System.out.println(stu1.equals(stu2));
        hm.put(stu1, 100); //once you put the key value in to hashmap, even you change the key, hashmap won't rehash the key for you
        stu1.id=2;
        System.out.println(hm.get(stu1));
    }
}
class Student {
    int id;
    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) { //default using the reference
                                        //when iterate through node array
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {   //default is using the addr/reference
                                // compute the index in the hashmap node array
//        return 1;
        int res=1;
        res= res*31+id;
        res= res*31+name.hashCode();
        return Objects.hash(id, name);//result   (31*1+ id)*31+ (name)
    }
}

/**
 * 5. TreeMap ordered
 * binary search
 *    left< root.val <right      left root right
 *    min heap
 *      root <left/right
 */
class TestTreeMap{


    public static void main(String[] args) {
        //3 2 1 null
        TreeMap<Integer, Integer> treeMap = new TreeMap<>((k1,k2)->{
            if(k1==k2) return 0;
            if(k1==null) {
                return 1;
            }else if(k2==null) {
                return -1;
            }
            return k2-k1;
        });
        treeMap.put(1,null);
        treeMap.put(3,null);
        treeMap.put(2,null);
        treeMap.put(null, null);
        System.out.println(treeMap);
    }
}

/**
 * 6.HashSet
 *  don't allow duplicate
 *  don't have insertion order
 */


/**
 * 7. TreeSet
 */
class TestSet{
    public static void main(String[] args) {
        TreeSet<String> ts = new TreeSet<>();
        //default ascending
        ts.add("a");
        ts.add("c");
        ts.add("b");
        ts.add("d");
        System.out.println(ts);
    }
}
/**
 * heap insert into heapy log(n)
 */

class TestHeap{

}




