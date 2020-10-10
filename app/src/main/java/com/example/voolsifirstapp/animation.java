package com.example.voolsifirstapp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class animation extends AppCompatActivity {

    ImageView image;
    RelativeLayout rellay6;
    TextView text;
    Button _btndone, _btnstart, _btnlogout;

    AnimationDrawable Animation;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        image= (ImageView) findViewById(R.id.image);
        rellay6= (RelativeLayout) findViewById(R.id.rellay6);
        text= (TextView) findViewById(R.id.text);
       // _btndone= (Button) findViewById(R.id.btndone);
        _btnstart= (Button) findViewById(R.id.btnstart);
      //  _btnlogout= (Button) findViewById(R.id.btnlogout);

        image.setBackgroundResource(R.drawable.animation);
        Animation = (AnimationDrawable) image.getBackground();

        _btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(animation.this, bluetooth.class);
                startActivity(intent);

            }
        });

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Animation.start();
    }



}
