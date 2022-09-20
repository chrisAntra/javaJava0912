package week2;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Day6 {
    volatile int i;
    public static void main(String[] args) {
    }
}
/**
 *  code
 *  gc
 *  jvm tranlater
 *  machine
 */
/**
 *
 * volatile  + cas compare and set/swap (atomic instruction)
 *      i=0
 *      t1          t2
 *      0            0
 *    exp=0       exp=0
 *  1 if cur==exp(succ)  1 if cur!=exp(fail)
 *                  exp=1
 *                  2 if cur==exp
 *  compareAndSet(Object, offset, expectValue(Prev Read Value), NewValue);
 *                  target attribute
 *         optimistic lock
 *              pros:
 *                  more read than write, improve performance
 *              cons:
 *                  a lot write op, waste cpu
 */

/**
 * ThreadSafe Library
 *      HashTable
 *          synchronized the method(on the whole table)
 *      ConcurrentHashMap
 *          synchronized on the Node
 *          Node[]
 *      Stack
 */

//Main thread(worker) execute your public static void main(String[] args)
//t1
//t2
class ThreadSafeLibrary {
    static AtomicInteger ai = new AtomicInteger(0);
    public static void main(String[] args) throws Exception{

        Thread t1 = new Thread(()->{
            for(int i=0;i<10000;i++) {
                ai.incrementAndGet();
            }
        });
        Thread t2 = new Thread(()->{
            for(int i=0;i<10000;i++) {
                ai.incrementAndGet();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(ai.get());
    }
}
/**
 * Reentrant Lock
 *
 *      fair lock
 *      multip wl (condition)
 *      tryLock
 *
 *      condition (waitlist)
 *      creating critical code block
 *      lock()
 *      lock()
 *      unlock()
 *      unlock()
 * compare to synchronized
 *       same: create critical code block: pessimistic lock
 *       diff: synchr doesn't have fair lock, provide multip wl
 */
class TestReentrantLock{
    //t1(A) t2(B) t3(C), A B C ...
    static ReentrantLock lock = new ReentrantLock(true);
    static Condition t1wl = lock.newCondition();
    static Condition t2wl = lock.newCondition();
    static Condition t3wl = lock.newCondition();

    public static void main(String[] args) {

        Thread t1 = new Thread(()->{
            try{
                lock.lock();
                for(int i=0;i<10;i++) {

                    System.out.println("A");
                    t2wl.signal();
                    t1wl.await();
                }
                lock.unlock();
            }catch (Exception ex){
                System.out.println(ex);
            }
        });
        Thread t2 = new Thread(()->{

            try{
                Thread.currentThread().sleep(10);
                lock.lock();
                for(int i=0;i<10;i++) {

                    System.out.println("B");
                    t3wl.signal();
                    t2wl.await();
                }
                lock.unlock();
            }catch (Exception ex){
                System.out.println(ex);
            }
        });
        Thread t3 = new Thread(()->{
            try{
                Thread.currentThread().sleep(20);
                lock.lock();
                for(int i=0;i<10;i++) {

                    System.out.println("C");
                    t1wl.signal();
                    t3wl.await();
                }
                lock.unlock();
            }catch (Exception ex){
                System.out.println(ex);
            }
        });
        t1.start();
        t2.start();
        t3.start();


    }
}
class Test11 {
    public static void main(String[] args) {
        Boolean fair = false;
        System.out.println(fair?"This is true":"This is false");
    }
}
/**
 * DeadLock
 *      2 resource
 *      lock1 lock2
 *      t1                t2
 *      lock1             lock2           -> lock1
 *      try get lock2     try to get lock1 ->lock2
 *
 *      Sol1: release the lock you current holding after a timeout
 *      Sol2: lock in same order
 *      *Sol3: create a look up table to check which thread hold which lock, and prevent deadlock happened
 *
 *
 */
class Test12 {
    public static void main(String[] args) {
        Object l1 = new Object();
        Object l2 = new Object();
        Thread t1 =new Thread(()->{
            synchronized (l1){
                try{
                    Thread.currentThread().sleep(1000);
                }catch (Exception ex){}

                synchronized (l2){
                    System.out.println("real work in t1");
                }
            }
        });
        Thread t2 =new Thread(()->{
            synchronized (l1){
                try{
                    Thread.currentThread().sleep(1000);
                }catch (Exception ex){}
                synchronized (l2){
                    System.out.println("real work in t2");

                }
            }
        });
        t1.start();
        t2.start();
    }
}
/**
 * Blocking Queue
 *      like a buffer
 *    producer(t1)->add ele into queue  [][][][][]  -> consumer(t2)
 *    while the queue if full, we will block the producer
 *    while the queue if empty, we will block the consumer
 *
 */

/**
 * ThreadPool
 *      creation new thread is expensive
 *      use a pool to cache
 *      reuse the worker thread
 *
 *       Executor                       Executors
 *
 *                 create threadPool:
 *                  ThreadPoolExecutor
 *                  ScheduledThreadPoolExecutor
 *                  ForkJoinPool
 *                      forkJoinTask(Big) -> t0 -> steal multip thread -> join into one result
 *                                           t1
 *                                           t2
 *                                           ...
 *            List<Integer>  {0,123,123,12,3}  ->{0, 123}           -> {0, 123^2, 123^2, 12^2, 3^2}
 *                                               {123, 12}
 *                                               {3}
 *
 *
 */
class TestThreadPool {
    public static void main(String[] args) {
        Executor threadPool = Executors.newFixedThreadPool(1);
        Executors.newSingleThreadExecutor();
        Executors.newCachedThreadPool();
        //
        ScheduledExecutorService scheduledExecutorService =Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println("xxxx1");
        }, 1, 2, TimeUnit.SECONDS);
        ForkJoinPool forkJoinPool = new ForkJoinPool();


//        threadPool.execute(()->{
//            try{
//                Thread.currentThread().sleep(1000);
//            }catch (Exception ex){}
//            System.out.println(Thread.currentThread().getName()+" I am a task0");
//        });
//        threadPool.execute(()->{
//            try{
//                Thread.currentThread().sleep(1000);
//            }catch (Exception ex){}
//            System.out.println(Thread.currentThread().getName()+" I am a task1");
//        });
//        threadPool.execute(()->{
//            try{
//                Thread.currentThread().sleep(1000);
//            }catch (Exception ex){}
//            System.out.println(Thread.currentThread().getName()+" I am a task2");
//        });
    }

}

/**
 * Future
 */
class TestFuture{
    public static void main(String[] args) throws Exception{

        ExecutorService threadPool = Executors.newSingleThreadExecutor();
//        Future future = threadPool.submit(()->{
//            try{
//                Thread.currentThread().sleep(3000);
//            }catch (Exception ex){
//                System.out.println(ex);
//            }
//
//            System.out.println("some work");
////            throw new RuntimeException("customized runtime");
//        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    throw new FileNotFoundException();
                }catch (Exception ex){}

            }
        };
        Callable<Integer> callableTask = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new FileNotFoundException("xxxx");

            }
        };
        Future<Integer> future = threadPool.submit(callableTask);
        Integer result = future.get(); // blocking method block the current thread(Main)
        System.out.println(result);
        //...operation

        System.out.println(result *2);
//        while(!future.isDone()){
//            System.out.println("work didn't finished");
//            Thread.currentThread().sleep(1000);
//        }
//        System.out.println("future task is done!");
    }
}

//Java 8

/**
 * Functional Interface
 */

/**
 * Lambda Expression
 */

/**
 * Stream API
 * Parallel Stream API
 */

class Test {
    public static void main(String[] args) {
        IntStream.range(0,10).forEach(System.out::println);
    }
}

/**
 * Optional
 */

/**
 * Completable Future
 */

