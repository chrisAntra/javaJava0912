package week1;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day1 {
}
/**
 *1. OOP
 *      class blue print
 *          attribute
 *          method
 *      instance
 *
 *      polymorphism
 *          instance
 *              Upcasting:
 *                  provide child type object to the parent type reference variable
 *          method
 *              compile time: method overloading
 *              run time: method overriding
 *      encapsulation
 *          access modifier:
     *          private
     *          default
     *          protected
     *          public
 *      abstraction
 *          abstract class
 *              similiar to class doesn't support multi inherit
 *          interface
 *              support multi inherit
 *              before java 8:
 *                  only abtract
 *              after:
 *                  default
 *                  static
 *                  private
 *      inheritance
 *          reuse
 */
class PolyTest {

    public static void main(String[] args) {
        //reference variable
        //
        List<Integer> list = new LinkedList<>();
        list = new ArrayList<>();
//        LinkedList<Integer> list1 = new LinkedList<>();
//        PolyTest polyTest = new PolyTest();
//        polyTest.dummy(true);
    }

    void dummy(int i){
//        dummy(true);
        System.out.println("hello1");
    }
    void dummy(boolean b){
        System.out.println("hello2");
    }

}
class PolyTest1{}


//class PolyTestChild extends PolyTest{
//
//    @Override
//    void dummy(int i) {
//        System.out.println("In the child");
//    }
//    void justInChild(){}
//
//    public static void main(String[] args) {
//        // reference var(reference ->obj)
//        PolyTest polyTest = new PolyTestChild();
//        PolyTestChild polyTestChild = (PolyTestChild) polyTest;
//        polyTest.dummy(1);
//        A.dummy(1);
//    }
//}

//interface A{
//    int a=1;
//    default void dummy2(boolean b) {
//        dummy(1);
//    }
//    static void dummy(int i){
//        System.out.println("method int interface");
//    }
//
//}
//interface B{
//    int a=2;
//}
//class InheritTest implements A, B {
//    public static void main(String[] args) {
//        System.out.println(B.a);
//    }
//
//}
//
//class InheritTest1 implements A, B {
//    public static void main(String[] args) {
//        System.out.println(B.a);
//
//    }
//
//}
abstract class AbstactTest{
    int i;
    //constructor in the abstract class is just used in its child class
    AbstactTest(){
        System.out.println("this is default constr in Abstract Test");
    }
    AbstactTest(int i){
        this.i=i;
    }
    final int a=1;
    abstract void dummyAbstract();
    final void dummy1(){
        System.out.println("final method in abstract");
    }
}
abstract class AbstactTest1{
    abstract void dummyAbstract();
}


class Test1 extends AbstactTest{
    Test1(){
        System.out.println("a");
    }
    Test1(int i){
        super(1);
        //xx
    }

    void dummyAbstract(){}

    public static void main(String[] args) {
        Test1 t1 = new Test1(1);
    }

}

class EncapTest {
    public int i;

    public int getI() {
        return i;
    }
}

/**
 * 2.static
 *      the member directly related to the class/interface itself
 *      cannot inherit
 */

class A {
    //related to class
    static int a=1;
    //related to object
    int b=1;
    static void dummy(){
        System.out.println("in A");
    }
}

class AChild extends A {

    static void dummy() {
        System.out.println("in A Child");
    }

    public static void main(String[] args) {
        AChild aChild = new AChild();
        A a = new A();
        aChild.dummy();
    }
}
class A1Child extends A {
    public static void main(String[] args) {
        A1Child a1Child = new A1Child();
        A1Child a1Child1 = new A1Child();
        System.out.println(a1Child.b);
        a1Child.b =10;
        System.out.println(a1Child1.b);
    }

}

/**
 * 3. Java is pass by value
 *      primitive type
 *          int boolean byte char short long float double
 *      object
 *
 */

class Test2{
    public static void main(String[] args) {
        //rv(1)
        final int i=1;
        final List<Integer> list = new LinkedList<>();
        list.add(1);
        System.out.println(list);
        String s1 = "aab";
        s1 = s1+"a";
        //s1 "aaba"
        //1+2+3+4 +...n
        //n(n+1)/2
        //System.gc();
        StringBuilder res=new StringBuilder("");
        //space comp:O(n)
        /*
         */
        for(int j=s1.length()-1;j>=0;j--) {
            res.append(s1.charAt(j));
        }
        System.out.println(res);
        //"aab" "a" "aaba"
        System.out.println(s1);


        //rv(reference value(address))
        //Integer pool(range(-128~127))
        Integer l1 = 128;
        Integer l2 = 128;
        System.out.println(l1==l2);

    }

    void dummy(Person p) {
        p.i=1;
    }


}
class Person {
    int i;
}




