package com.example.voolsifirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.nio.ByteBuffer;



import static com.example.voolsifirstapp.Classifier.HEIGHT;
import static com.example.voolsifirstapp.Classifier.WIDTH;


public class MainActivityClassification extends AppCompatActivity {
    private static final String LOG_TAG = MainActivityClassification.class.getSimpleName();


    //@BindView(R.id.tv_prediction) TextView mTvPrediction;
    //@BindView(R.id.tv_probability) TextView mTvProbability;
    //@BindView(R.id.tv_timecost) TextView mTvTimeCost;



    private Classifier mClassifier;
    private ByteBuffer Data;

    TextView mTvPrediction;
    TextView mTvProbability;
    TextView mTvTimeCost;

    Button btn_detect;

    public MainActivityClassification() {
        Data = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvPrediction = (TextView) findViewById(R.id.tv_prediction);
        mTvProbability = (TextView) findViewById(R.id.tv_probability);
        mTvTimeCost = (TextView) findViewById(R.id.tv_timecost);
        //ButterKnife.bind(this);
        //init();

        btn_detect= (Button) findViewById(R.id.btn_detect);

    }

    void onDetectClick(byte[] mData) {
        if (mClassifier == null) {
            Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");
            return;
        } else if (mData==null) {
            Toast.makeText(this, R.string.please_restart, Toast.LENGTH_SHORT).show();
            return;
        }

        Data = Data.get(mData, WIDTH, HEIGHT);
        Result result = mClassifier.classify(Data);
        renderResult(result);
    }


    void onClearClick() {
        mTvPrediction.setText(R.string.empty);
        mTvProbability.setText(R.string.empty);
        mTvTimeCost.setText(R.string.empty);
    }

    private void init() {
        try {
            mClassifier = new Classifier(this);
        } catch (IOException e) {
            Toast.makeText(this, R.string.failed_to_create_classifier, Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "init(): Failed to create Classifier", e);
        }
    }

    private void renderResult(Result result) {
        mTvPrediction.setText(String.valueOf(result.getNumber()));
        mTvProbability.setText(String.valueOf(result.getProbability()));
        mTvTimeCost.setText(String.format(getString(R.string.timecost_value),
                result.getTimeCost()));
    }

}

