package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Date;
import java.util.Random;


public class Registration extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText edtlpin, edttpin, edtmobile, edtfname, edtlname, edtmail, edtmale, edtfemale;
    RadioGroup r_gender;
    int lpin;
    int tpin;
    String mail;
    String fname;
    String lname;
    String s_gender;
    Button reg_btn;
    long mobile;
    DatabaseHelper helper;
    SQLiteDatabase database;
    ProgressDialog progressDialog;
    long number;
    String mobilepattern="[0-9]{10}";
    String pinpattern="[0-9]{4}";
    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(Registration.this);
        edtlpin = (EditText) findViewById(R.id.r_lpin);
        edttpin = (EditText) findViewById(R.id.r_tpin);
        edtfname = (EditText) findViewById(R.id.r_fname);
        edtlname = (EditText) findViewById(R.id.r_lname);
        edtmail = (EditText) findViewById(R.id.r_email);
        edtmobile = (EditText) findViewById(R.id.r_mobileno);
        r_gender = (RadioGroup) findViewById(R.id.gender);
        r_gender.setOnCheckedChangeListener(this);
        reg_btn = (Button) findViewById(R.id.signup_btn);
        reg_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (edtfname.getText().toString().equals("")) {
            edtfname.setError("Enter First Name");
        } else if (edtlname.getText().toString().equals("")) {
            edtlname.setError("Enter last name");
        } else if (!(edtmail.getText().toString().matches(emailpattern))) {
            edtmail.setError("Enter Proper Email Address");
        } else if (!(edtmobile.getText().toString().matches(mobilepattern))) {
            edtmobile.setError("Enter Proper Mobile No.");
        } else if (!(edtlpin.getText().toString().matches(pinpattern))) {
            edtlpin.setError("Enter Only 4 digit Pin");
        } else if (!(edttpin.getText().toString().matches(pinpattern))) {
            edtmail.setError("Enter Only 4 digit pin");
        } else {

            progressDialog.show();
            mail = edtmail.getText().toString();
            lpin = Integer.parseInt(edtlpin.getText().toString());
            tpin = Integer.parseInt(edttpin.getText().toString());
            mobile = Long.parseLong(edtmobile.getText().toString());
            fname = edtfname.getText().toString();
            lname = edtlname.getText().toString();
            Random random = new Random();
            int account = random.nextInt(9999 - 1001) + 1001;
            helper = new DatabaseHelper(this);
            database = helper.getWritableDatabase();
            try {
                try {


                    Cursor c = database.rawQuery("select contact from customer where contact=" + mobile + "", null);
                    if (c != null) {
                        int contactindex = c.getColumnIndex("contact");
                        c.moveToFirst();
                        do {
                            number = Long.parseLong(c.getString(contactindex));
                            Log.d("number", String.valueOf(number));

                        } while (c.moveToNext());
                        if (mobile == number) {
                            Toast.makeText(Registration.this, "Cannot register Already Exist ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }
                catch (CursorIndexOutOfBoundsException c)
                {
                    String query = "insert into customer(email,lpin,tpin,contact,fname,lname,gender,accountno,balance) values('" + mail + "'," + lpin + "," + tpin + "," + mobile + ",'" + fname + "','" + lname + "','" + s_gender + "'," + account + ",0)";
                    database.execSQL(query);
                    progressDialog.dismiss();
                    Intent i = new Intent(Registration.this, MainActivity.class);
                    Toast.makeText(Registration.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();

                }
            } catch (NullPointerException e) {


                    String query = "insert into customer(email,lpin,tpin,contact,fname,lname,gender,accountno,balance) values('" + mail + "'," + lpin + "," + tpin + "," + mobile + ",'" + fname + "','" + lname + "','" + s_gender + "'," + account + ",0)";
                    database.execSQL(query);
                    progressDialog.dismiss();
                    Intent i = new Intent(Registration.this, MainActivity.class);
                    Toast.makeText(Registration.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();

                }
            }
        }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.male)
        {
            s_gender="Male";
        }
        else {
            s_gender="Female";
        }
    }
}
