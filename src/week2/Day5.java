package week2;

public class Day5 {

}

/**
 * Thread lifecycle (one time)
 *      1.New : new Thread();
 *      2.Active: thread.start();
 *      3.wait(): blocked/waiting
 *      4.sleep():Timed Waiting
 *      5.terminated
 */
/**
 * synchronize
 *      create a critical block, at same time, only one thread can work on it
 *      monitor of class or object
 *      drawback:
 *          slow down performance
 *      sleep() put the current thread sleep for the specifid time
 *      wait() let current thread to release the monitor it hold
 */

/**
 *  Race condition
 *  a=0,
 *  t1(+1), t2(+1)
 *  0    0
 *  1    1
 */
class TestRaceCondition{
    volatile static int a=0;

    static void increment(){
        a++;
    }
    public static void main(String[] args) throws Exception{
        Object obj = new Object(); //use the monitor of the obj
        Object obj1 = new Object();
        Thread t1 = new Thread(()->{

            for(int i=0;i<10000;i++) {
//                synchronized (obj){
//                    a++;
//                }
                increment();
            }
        });
        Thread t2 = new Thread(()->{
            for(int i=0;i<10000;i++) {
//                synchronized (obj1){
//                    a++;
//                }
                increment();

            }
        });
        t1.start();
        t2.start();
        // main t1 t2
        //block main thread here until t1 finished
        t1.join();
        //block main thread here until t2 finished
        t2.join();
        Thread.currentThread().sleep(1000);
//        TestRaceCondition.class.wait();
        System.out.println(a);
    }
}

//class TestRaceCondition{
//    static int a=0;
//
//    synchronized static void increment(){
//        try{
//            System.out.println("I am:"  +Thread.currentThread().getName());
//            TestRaceCondition.class.wait();//wait for further notify, wait will release the monitor
//            System.out.println("wake up");
//            for(int i=0;i<10000;i++) {
//                 a++;
//            }
//            TestRaceCondition.class.notify();
//            Thread.currentThread().wait();
//
//        }catch(Exception ex){}
//    }
//    public static void main(String[] args) throws Exception{
//        Object obj = new Object(); //use the monitor of the obj
//        Object obj1 = new Object();
//        Thread t1 = new Thread(()->{
//            increment();
//
//        });
////        Thread t2 = new Thread(()->{
////            for(int i=0;i<10000;i++) {
//////                synchronized (obj1){
//////                    a++;
//////                }
////                increment();
////
////            }
////        });
//        t1.start();
////        t2.start();
//        // main t1 t2
//        //block main thread here until t1 finished
////        t1.join();
//        //block main thread here until t2 finished
////        t2.join();
//        Thread.currentThread().sleep(1000);
//        synchronized (TestRaceCondition.class) {
//            TestRaceCondition.class.notify();
//            TestRaceCondition.class.wait();
//        }
//        System.out.println("out of synchronized");
////        TestRaceCondition.class.wait();
////        Thread.currentThread().sleep(1000);
////        t1.join();
//        System.out.println(a);
//    }
//}

//t1 print "A" notify() other thread which wait for this monitor, and then wait() put t1 into waiting state release the monitor
class TestWait{
    //t1 print A
    //t2 print B
    //t3 print C
    //ABCABACBCABC
    // ABABABABA
    public static void main(String[] args) throws  Exception{
        Object obj = new Object();
        Thread t1 = new Thread(()->{
            try{
                synchronized (obj){
                    for(int i=0;i<10;i++) {
                        System.out.println("A");
                        obj.notify();
                        obj.wait(); //put t1 waiting for obj monitor, release the currently holding monitor obj

                    }
                }
            }catch (Exception ex){}
        });
        Thread t2 = new Thread(()->{
            try{
                synchronized (obj){
                    for(int i=0;i<10;i++) {
                        System.out.println("B");
                        obj.notify();
                        obj.wait();

                    }
                }
            }catch (Exception ex){}
        });
        t1.start();
        t2.start();
        Thread.currentThread().sleep(3000);
        synchronized (obj){
            obj.notifyAll();
        }

    }
}

/**
 * t1->cache1->memory  <-cache2<-t2
 *volatile int i 1->10                        print(i) 10
 * volatile
 *      1.visibility
 *          write to attribute, directly write into memory
 *          read directly read from the memory
 *      2. m fence
 *      3. preventing optimizer from reordering code
 *
 *      itself cannot provide thread safe
 *
 *
 */
class TestVolatile{
    volatile static int i=1;

    public static void main(String[] args) {
//        System.out.println("step 1");
//        i=10;
////        System.out.println(i);
//        System.out.println("step 2");

        Thread t1 = new Thread(()->{
            try{
                Thread.currentThread().sleep(3000);
            }catch (Exception ex){}
            System.out.println("wake up and change to 10");
            i=10;
        });
        Thread t2 = new Thread(()->{
            while(i==1){
            }
            System.out.println("In t2: "+i);
        });
        t1.start();
        t2.start();
    }
}
class TestJoin{
    static int a=0;
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            try{
                Thread.currentThread().sleep(4000);
                for(int i=0;i<900000000;i++){
                    a++;
                }
            }catch(Exception ex){}

        });
        Thread t2 = new Thread(()->{
            try{
                System.out.println("t2 finshed working");
            }catch(Exception ex){}
        });
        Thread t3 = new Thread(()->{
            try{
                t1.join();
                System.out.println(a);
                System.out.println("finished waiting for t1");
            }catch (Exception ex){}
        });
        t1.start();
        t2.start();
        t3.start();
    }

}