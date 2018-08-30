package com.hsy.Processor;

import android.util.Log;

/**
 * Created by hsy on 18-8-20.
 */

public class ProcessorForTest<T> extends Processor<T> {
    public static final String TAG = "ProcessorForTest";

    @Override
    protected T process(T m) {
//        long result = 1;
//        long startTime = System.currentTimeMillis();
//        long i = 88888;
//        for (int j = 0; j < i; j++){
//            result += Math.sqrt(i);
////            result = (long)Math.sqrt(i);
//            for (int k = 1; k < 3; k ++){
//                result = (long)Math.pow(result, k);
//            }
//        }
////        Log.d(TAG, "process: " + (startTime - System.currentTimeMillis()) + "ms");
//        Log.d(TAG, "process: one " + (System.currentTimeMillis() - startTime) + "ms");
//        return result;
        return m;
    }

    @Override
    protected void onOutputThread(T temp){

    }
}
