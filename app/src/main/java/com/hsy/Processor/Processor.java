//package com.hsy.testrenderscript;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by hsy on 18-8-18.
// */
//
//public abstract class Processor<E> {
//    public static final String TAG = "Processor";
//    private ExecutorService executorService = Executors.newFixedThreadPool(10);
//    public BlockingQueue<E> inputBuffer = new ArrayBlockingQueue<E>(20);
//    public BlockingQueue<E> outputBuffer = new ArrayBlockingQueue<E>(20);
//
//    public void testExecutorService() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    while (!inputBuffer.isEmpty()) {
//                        executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                E temp = null;
//                                if((temp = inputBuffer.poll()) != null){
//                                    process(temp);
//                                    outputBuffer.offer(temp);
//                                }
////                        inputBuffer.
////                        outputBuffer.add()
//                            }
//                        });
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public E getOutput() {
//        if (!outputBuffer.isEmpty()) {
//            return outputBuffer.poll();
//        }
//        return null;
//    }
//
//    public abstract void process(E temp);
//
//}

package com.hsy.Processor;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hsy on 18-8-18.
 */

public abstract class Processor<T> {
    public static final String TAG = "Processor";
    public static int NUMBER_OF_THREAD = 9;
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
    public static BlockingQueue inputBuffer = new LinkedBlockingQueue(NUMBER_OF_THREAD);
    public static BlockingQueue outputBuffer = new LinkedBlockingQueue(NUMBER_OF_THREAD);

    public Processor() {
        getReady();
        runOutputThread();
    }

    private void getReady() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!inputBuffer.isEmpty()) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                T temp = null;
//                                synchronized (this) {
                                try {
                                    temp = (T) inputBuffer.take();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                }
                                temp = process(temp);
//                                synchronized (this) {
                                try {
                                    outputBuffer.put(temp);
//                                    System.gc();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void runOutputThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                         onOutputThread( (T) outputBuffer.take());
//                            count++;
//                        Log.d(TAG, "run: parallel result " + i);
//                        Log.d(TAG, "run: parallel " + (System.currentTimeMillis() - startTime) + "ms");
//                            System.gc();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected abstract void onOutputThread(T temp);

    protected abstract T process(T temp);

}
