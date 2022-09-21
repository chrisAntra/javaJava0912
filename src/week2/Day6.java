package week2;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
 * Future (wrapper)
 *  get() blocking
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

/**
 *  Java 8
 *      interface
 *          default static method
 *      time
 *      Functional Interface
 *      Lambda Expression
 *      Stream API
 *      Optional
 *      Completable Future
 */

/**
 * Functional Interface
 *      interface only contains one abstract method
 */
interface  NormalInterface{
//    void method0();
//    void method1();
//    void method2();
    static void method3(){

        System.out.println("xxx");
    }

    private void method4(){
        System.out.println("method4");
    }
    default void method5(){
        method4();
    }

}
interface  NormalInterface2{
    //    void method0();
//    void method1();
//    void method2();
//    static void method3(){
//
//        System.out.println("xxx");
//    }
//
//    private void method4(){
//        System.out.println("method4");
//    }
    default void method5(){
        System.out.println("i am in nor2");
    }

}
class Test1111{
    static class Cld implements NormalInterface, NormalInterface2{
        @Override
        public void method5(){
            System.out.println("something");
        }
    }
    public static void main(String[] args) {
        Cld cld =new Cld();
        cld.method5();
        NormalInterface.method3();
    }
}
@FunctionalInterface
interface MyFuncitonInterface{
    void method1();
    static void method2(){};
}
class MyClzz implements Runnable{
    @Override
    public void run() {

    }
}

/**
 * Runnable vs Callable
 *  void         return<>
 *  /            throws handle checked exception
 */
class Test71{
    public static void main(String[] args) {
//        Runnable runnable1 = new MyClzz();
//        //anonymous class implement the runnable interface, then new an object from that anonymous class
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        //
//        Callable callable = new Callable() {
//            @Override
//            public Object call() throws Exception {
//                return null;
//            }
//        };
        Predicate<Integer> predicate = a-> a>10;
        System.out.println(predicate.test(5));

        Comparator<Integer> comparator = (a, b)-> a-b;
        System.out.println(comparator.compare(10, 1));

        BiFunction<Integer, Boolean, Object> biFunction = (x1, x2)->{
            return new Object();
        };
        System.out.println(biFunction.apply(1,true));
        Supplier<HashMap> supplier = ()-> new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> res = supplier.get();
        //Consumer
//        MyFuncitonInterface myFuncitonInterface = ()->{
//            System.out.println("overring my customized fi");
//        };
//        myFuncitonInterface.method1();
    }
}

/**
 * Lambda Expression (only work with functional interface)
 *  (input rv)-> { method body}
 *  if there one input
 *   ...-> ...
 */

/**
 * Stream API
 *      for loop
 *          Stream obj(contains ele in your collections) stream api apply operation on each of the ele
 *      chain operation
 *      intermediate step (map(), mapToInt(), filter()...)
 *      terminate step(collect(), reduce(), forEach())
 * Parallel Stream API
 */
class Test72{
    public static void main(String[] args) {
//        Function function
        List<Integer> list = Arrays.asList(1,2,3,4,5);
               //stream obj

        int i=(int) list.parallelStream().
//                map(x->x+10). //get new value into the stream
//                map(x->x*10).
                                           // 10 20 30 40 5
               //reduce the stream obj into one object
                map(x->{
                   System.out.println(Thread.currentThread().getName()+" working on "+x);
                    return x*10;
               }).
                //100+1
                //100+2
                //100+3
                reduce(0,
                        (res, x)-> {
                            System.out.println(Thread.currentThread().getName()+" working on "+res+", "+x);
                            return res+x;
                        },
                        (finalR, r)->finalR+r);
        System.out.println(i);


    }
}

/**
 * filter the employee age>=25
 */
class Employee {
    int age;

    public Employee(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "age=" + age +
                '}';
    }
}
class Test73{
    public static void main(String[] args) {
        List<Employee> employeeList = new LinkedList<>();
        new Employee(18);
        employeeList.add(new Employee(18));
        employeeList.add(new Employee(20));
        employeeList.add(new Employee(25));
        employeeList.add(new Employee(30));
        employeeList.add(new Employee(35));
//        List<Employee> result = new LinkedList<>();
        //ele -> new ele
        List<Employee> res = employeeList.stream().filter(emp->emp.age>=25).map(emp->{
            System.out.println(Thread.currentThread().getName()+" working on "+emp);
            emp.age+=1;
            return emp;
        }).collect(Collectors.toList());
        System.out.println(res);


//        for(Employee employee: employeeList){
//            if(employee.age>=25){
//                result.add(employee);
//            }
//        }

    }


}

class Test {
    public static void main(String[] args) {
        IntStream.range(0,10).forEach(System.out::println);
    }
}

/**
 * Optional
 *      wrapper class
 *      wrap object
 *      help with null check
 *      eliminate all the "null" in code
 */
class Test74{
    public static void main(String[] args) throws Exception{
        Employee emp = null;
        Optional<Employee> optionalEmployee = Optional.of(emp);
        if(!optionalEmployee.isPresent()){
            System.out.println("Emp is null");
        }
    }
}

/**
 * Completable Future (multithreading library)
 *      fully asynchronous
 *      chain operation
 *      combine all the result from multiple cf
 *          allOf anyOf
 *
 */

class Test75 {
    public static void main(String[] args) throws  Exception{
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
//            try{
//                Thread.currentThread().sleep(3000);
//            }catch(Exception ex){
//                System.out.println(ex);
//            }
//            return 10;
//        });
////        int i= completableFuture.get();
//        completableFuture.thenAccept((x)->{
//            x+=10;
//            System.out.println(x);
//        });
//        CompletableFuture cf = CompletableFuture.supplyAsync(()->{
//            try{
//                Thread.currentThread().sleep(3000);
//            }catch(Exception ex){
//                System.out.println(ex);
//            }
//            return 10;
//        }).thenApply((x)->x+10).thenAccept(x->{
//            System.out.println(x);
//        });
//        System.out.println("this is main");
//        System.out.println("finished all work in main");
//        cf.join();
        List<CompletableFuture> completableFutures = new LinkedList<>();
        for(int i=0;i<5;i++) {
            final int fi=i;
            CompletableFuture<Integer> tempCf = CompletableFuture.supplyAsync(()->{
                System.out.println("STEP1: send apis calla and gather res");
                try{
                    Thread.currentThread().sleep(fi*1000);
                }catch (Exception ex){
                    System.out.println(ex);
                }
                return fi;
            });
            completableFutures.add(tempCf);
        }
        CompletableFuture signalCf = CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0]));
        signalCf.thenAccept(Void->{
            for(int i=0;i<5;i++){
                CompletableFuture<Integer> interEleCf = completableFutures.get(i);
                interEleCf.thenAccept(x->{
                    x+=10;
                    System.out.println("STEP2:"+ x);
                }).join();

            }
        }).join();
/**
 *  basic query
 *      select
 *      from
 *      where
 *      subQuery
 *      minus
 *      union
 *      intersect
 *      join
 *      group by
 *      aggregation funct min max avg
 *      execution
 *      select 2nd salary emp in table
 *  what transaciton ACID
 *  Table Design
 *      stu class
 *      1 -  1
 *      m -  1
 *      m -  m
 *  Normalization
 *  Index
 *      b/b+ tree
 *
 */

    }
}

