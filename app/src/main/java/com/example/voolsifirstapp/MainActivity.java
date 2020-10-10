package com.example.voolsifirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RelativeLayout rellay1,rellay2;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnreg, _btnlogin;
    EditText _txtfname, _txtlname, _txtemail, _txtpass, _txtphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper=new DatabaseHelper(this);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000);

        _btnreg = (Button) findViewById(R.id.btnreg);
        _btnlogin = (Button) findViewById(R.id.btnlogin);
        _txtfname = (EditText) findViewById(R.id.txtfname);
        _txtlname = (EditText) findViewById(R.id.txtlname);
        _txtemail = (EditText) findViewById(R.id.txtemail);
        _txtpass = (EditText) findViewById(R.id.txtpass);
        _txtphone = (EditText) findViewById(R.id.txtphone);

        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openHelper.getWritableDatabase();
                String fname=_txtfname.getText().toString();
                String lname=_txtlname.getText().toString();
                String email=_txtemail.getText().toString();
                String pass=_txtpass.getText().toString();
                String phone=_txtphone.getText().toString();

                insertdata(fname, lname, email, pass, phone);
                Toast.makeText(getApplicationContext(), "register successfully", Toast.LENGTH_LONG).show();
            }

        });
        _btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });


    }

    public void insertdata (String fname, String lname, String email, String pass, String phone){

        ContentValues contentValues= new ContentValues();
        contentValues.put (DatabaseHelper.COL_2, fname);
        contentValues.put (DatabaseHelper.COL_3, lname);
        contentValues.put (DatabaseHelper.COL_4, email);
        contentValues.put (DatabaseHelper.COL_5, pass);
        contentValues.put (DatabaseHelper.COL_6, phone);

        Long id= db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

    }
}
