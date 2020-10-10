package com.example.voolsifirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Classifier {
    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private static final String MODEL_NAME = "best_model_Swell.tflite";

    private static final int BATCH_SIZE = 1;
    public static final int HEIGHT = 34;
    public static final int WIDTH = 1;
    private static final int NUM_CHANNEL = 1;
    private static final int NUM_CLASSES = 3;

    private final Interpreter.Options options = new Interpreter.Options();
    private final Interpreter mInterpreter;
    public  final ByteBuffer mData;
    //private final int[] mPixels = new int[HEIGHT * WIDTH];
    private final float[][] mResult = new float[1][NUM_CLASSES];

    public Classifier(Activity activity) throws IOException {
        mInterpreter = new Interpreter(loadModelFile(activity), options);
        mData = ByteBuffer.allocateDirect(
                4 * BATCH_SIZE * HEIGHT * WIDTH * NUM_CHANNEL);
        mData.order(ByteOrder.nativeOrder());
    }

    public Result classify(ByteBuffer mData) {
        convertDataToByteBuffer(mData);
        long startTime = SystemClock.uptimeMillis();
        mInterpreter.run(mData, mResult);
        long endTime = SystemClock.uptimeMillis();
        long timeCost = endTime - startTime;
        Log.v(LOG_TAG, "classify(): result = " + Arrays.toString(mResult[0])
                + ", timeCost = " + timeCost);
        return new Result(mResult[0], timeCost);
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_NAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertDataToByteBuffer(ByteBuffer data) {

        /*if (mData == null) {
            return;
        }*/
        Context context = null;

        try {
            InputStream normalData= context.getAssets().open("normalData");
            int size= normalData.available();

            //Read the entire asset into a local byte buffer
            byte[] mData=new byte[size];

            normalData.read(mData);
            normalData.close();
            //mData.rewind();

        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        }

        //bitmap.getPixels(mPixels, 0, bitmap.getWidth(), 0, 0,
               // bitmap.getWidth(), bitmap.getHeight());

        /*int pixel = 0;
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                int value = mPixels[pixel++];
                mData.putFloat(convertPixel(value));
            }
        }*/
    }

    /*private static float convertPixel(int color) {
        return (255 - (((color >> 16) & 0xFF) * 0.299f
                + ((color >> 8) & 0xFF) * 0.587f
                + (color & 0xFF) * 0.114f)) / 255.0f;
    }*/
}
