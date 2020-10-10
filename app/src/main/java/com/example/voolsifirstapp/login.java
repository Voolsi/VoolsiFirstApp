package com.example.voolsifirstapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class login extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    RelativeLayout rellay3,rellay4;
    Button _btnlogin2;
    EditText _txtname, _txtpassword;
    Cursor cursor;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        _btnlogin2 = (Button) findViewById(R.id.btnlogin2);
        _txtname = (EditText) findViewById(R.id.txtname);
        _txtpassword = (EditText) findViewById(R.id.txtpassword);
        rellay3 = (RelativeLayout) findViewById(R.id.rellay3);
        rellay4 = (RelativeLayout) findViewById(R.id.rellay4);

        _btnlogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username= _txtname.getText().toString();
                String Password= _txtpassword.getText().toString();
                cursor= db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_4 + " =? AND " + DatabaseHelper.COL_5 + " =? " , new String[] {Username, Password} );
                if (cursor!=null) {
                    if (cursor.getCount()>0){
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "login successfully", Toast.LENGTH_LONG).show();

                        Intent intent= new Intent(login.this, animation.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "error: wrong name or password", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }
}
