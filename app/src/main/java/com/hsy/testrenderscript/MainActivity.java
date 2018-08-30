package com.hsy.testrenderscript;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hsy.Processor.Processor;
import com.hsy.Processor.ProcessorForTest;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public Button testButton;

    public long startTime = 0;

    //region processor test
    public Processor<Integer> processor;
    public static int count = 1;
    public static int NUMBER_OF_DATA = 5;

    TimerTask testProcessor = new TimerTask() {
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            try {
//                for (int i = 100000; i < 110000; i+=20000) {
//                    processor.inputBuffer.put(123456);
//                }
//                if(processor.inputBuffer.size() < 100)
                processor.inputBuffer.put(count++);

            } catch (Exception e) {
                Log.e(TAG, "onClick: ", e);
                e.printStackTrace();
            }

        }
    };
    Timer timer = new Timer();

    private void processorOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                int count = 0;
                while (true) {

                    try {
                        long i = (long) processor.outputBuffer.take();
//                            count++;
                        Log.d(TAG, "run: parallel result " + i);
                        Log.d(TAG, "run: parallel " + (System.currentTimeMillis() - startTime) + "ms");
//                            System.gc();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    if (count == NUMBER_OF_DATA) {
//                        count = 0;
//                        Log.d(TAG, "run: parallel " + (System.currentTimeMillis() - startTime) + "ms");
//                    }
                }
            }
        }).start();
    }

//    TimerTask testRenderScript = new TimerTask() {
//        @Override
//        public void run() {
//            testRenderScript();
//        }
//    };
//    Timer timer = new Timer();

    //endregion

    //region RenderScript test
    private void testRenderScript() {
//        Log.d(TAG, "testRenderScript: " + Runtime.getRuntime().availableProcessors() + "");
        int length = 10;
        long test_data = 12345;
        long[] inputArray = new long[length];
        for (int i = 0; i < length; i++) {
            inputArray[i] = test_data + 1;
        }
        // Instantiates the input Allocation, that will contain our sample
        // numbers.
        long startTime = System.currentTimeMillis();
        RenderScript renderScript = RenderScript.create(this);
        ScriptC_test myScript = new ScriptC_test(renderScript);
        Allocation inputAllocation = Allocation.createSized(renderScript, Element.I64(renderScript), length);
        inputAllocation.copyFrom(inputArray);
        Allocation outputAllocation = Allocation.createSized(renderScript, Element.I32(renderScript), length);
        //创建ScriptC脚本，名字为ScriptC_文件名。
        myScript.forEach_invert(inputAllocation, outputAllocation);
        int[] outputArray = new int[length];
        outputAllocation.copyTo(outputArray);
//        Log.d("tag", "time = " + (System.currentTimeMillis() - startTime));
        Log.d(TAG, "testRenderScript: Time" + "1 " + (System.currentTimeMillis() - startTime) + "ms");
        inputAllocation.destroy();
        outputAllocation.destroy();
        myScript.destroy();
        renderScript.destroy();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < length; i++)
            inputArray[i] = rsCalculateFunction(test_data);
//        Log.d("tag", "time = " + (System.currentTimeMillis() - startTime));
        Log.d(TAG, "testRenderScript: Time" + "2 " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private long rsCalculateFunction(long i) {
        startTime = System.currentTimeMillis();
        long result = 1;
        for (int j = 0; j < i; j++) {
//            result += Math.sqrt(i);
//                                result = (long)Math.sqrt(i);
            for (int k = 1; k < 3; k++) {
                result = (long) Math.pow(result, k);
            }
        }
        Log.d(TAG, "rsCalculateFunction: Time" + (System.currentTimeMillis() - startTime) + "ms");
        return result;
    }

    private class RsCalculateThread extends Thread {
        @Override
        public void run() {
            testRenderScript();
        }
    }

    private RsCalculateThread rsCalculateThread = new RsCalculateThread();

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
//        processor = new ProcessorForTest<Integer>();
//        processorOut();
    }

    protected void initUI() {
        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region pipeline test
                Thread serial = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startTime = System.currentTimeMillis();
                        long result1 = 1;
                        int i1 = 100000;
                        for (int j = 0; j < i1; j++) {
                            result1 += Math.sqrt(i1);
//                                result = (long)Math.sqrt(i);
                            for (int k = 1; k < 4; k++) {
                                result1 = (long) Math.pow(result1, k);
                            }
                        }
                        Log.d(TAG, "run: single " + (System.currentTimeMillis() - startTime) + "ms");
                        startTime = System.currentTimeMillis();
                        for (int i = 100000; i < 110000; i += 2000) {
                            long result = 1;
                            for (int j = 0; j < i; j++) {
                                result += Math.sqrt(i);
//                                result = (long)Math.sqrt(i);
                                for (int k = 1; k < 3; k++) {
                                    result = (long) Math.pow(result, k);
                                }
                            }
                            Log.d(TAG, "run: serial result " + result);
                        }
                        Log.d(TAG, "run: serial " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                });
//                serial.start();
//                try {
//                    serial.join();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }


//                Thread parallel = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true){
//                            try {
//                                startTime = System.currentTimeMillis();
//                                for (int i = 100000; i < 110000; i+= 2000) {
//                                    processor.inputBuffer.put(i);
//                                }
//                            } catch (Exception e) {
//                                Log.e(TAG, "onClick: ", e);
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//                parallel.start();
//                timer.schedule(testProcessor, 0, 30);
                //endregion
                try {
                    if (!rsCalculateThread.isAlive()) {
                        rsCalculateThread = new RsCalculateThread();
                        rsCalculateThread.start();
                    } else
                        Log.d(TAG, "onClick: " + "rsCalculateThread is running.");
                } catch (Exception e) {
                    Log.e(TAG, "onClick: " + e.getMessage());
                }

            }
        });
    }

}
