package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtphone,edtpin;
    TextView fpass,signup;
    Button login;
    String dpin,did;
    DatabaseHelper helper;
    SQLiteDatabase database;
    ProgressDialog progressDialog;
    String num,pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtphone=(EditText) findViewById(R.id.username);
        edtpin=(EditText) findViewById(R.id.pin);
        signup=findViewById(R.id.txtsignup);
        fpass=findViewById(R.id.fpassword);
        helper=new DatabaseHelper(this);
        database=helper.getWritableDatabase();
        progressDialog = new ProgressDialog(MainActivity.this);
        login=(Button)findViewById(R.id.login_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Registration.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                num=edtphone.getText().toString();
                pin=edtpin.getText().toString();

                try {
                    try {


                        Cursor c = database.rawQuery("select id,lpin from customer where contact='" + num + "'", null);
                        if (c != null) {
                            int idindex = c.getColumnIndex("id");
                            int lpinindex = c.getColumnIndex("lpin");
                            c.moveToFirst();
                            do {
                                dpin = c.getString(lpinindex);
                                did = c.getString(idindex);
                            } while (c.moveToNext());
                        }
                    }catch (CursorIndexOutOfBoundsException c)
                    {
                        Toast.makeText(MainActivity.this,"Phone Number is Wrong",Toast.LENGTH_SHORT).show();
                    }
                    if (!(pin.equals(dpin))) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Failed Pin Wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Intent i = new Intent(MainActivity.this, Home.class);
                        i.putExtra("phone", num);
                        startActivity(i);
                        finish();
                    }
                }catch (Exception e)
                {

                }

            }
        });

    }

}
