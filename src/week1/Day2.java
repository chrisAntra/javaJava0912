package week1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
 * List
 *  LinkedList node(pointer prev, next)
 *      addLast O(1)
 *      add(index, item) O(n) traverse O(1) manipulate pointer
 *      get() O(n) :traverse through first/last O(n) next...next prev.prev
 *  ArrayList Object[]
 *      change size: exceed size -> grow O(n)
 *      get() O(1)
 */
class Test23 {
    public static void main(String[] args) {
        List<Integer> list = new LinkedList<>();
        list = new ArrayList<>();
        int i=2;

        System.out.println(i);


    }
}

