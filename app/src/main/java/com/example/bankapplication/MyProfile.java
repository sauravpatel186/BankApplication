package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class MyProfile extends AppCompatActivity {
    private EditText fname,acno,mobile,mail;
    private SQLiteDatabase database;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        fname=findViewById(R.id.pfname);
        acno=findViewById(R.id.pacno);
        mobile=findViewById(R.id.pmobile);
        mail=findViewById(R.id.pmail);

        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        database=databaseHelper.getWritableDatabase();

        id=getIntent().getStringExtra("id");

        Cursor c=database.rawQuery("select fname,lname,accountno,email,contact from customer where id='"+id+"'",null);
        if(c!=null)
        {
            c.moveToFirst();
            do {
                fname.setText(c.getString(c.getColumnIndex("lname"))+" "+c.getString(c.getColumnIndex("fname")));
                fname.setFocusable(false);
                acno.setText(c.getString(c.getColumnIndex("accountno")));
                acno.setFocusable(false);
                mail.setText(c.getString(c.getColumnIndex("email")));
                mail.setFocusable(false);
                mobile.setText(c.getString(c.getColumnIndex("contact")));
                mobile.setFocusable(false);
            }while (c.moveToNext());
        }
        c.close();
        database.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
      MenuInflater inflater=getMenuInflater();
      inflater.inflate(R.menu.change_pin,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.changelpin)
        {
            Intent i=new Intent(MyProfile.this, changeloginpin.class);
            i.putExtra("id",id);
            startActivity(i);
        }
        if(item.getItemId()==R.id.changetpin)
        {
            Intent f=new Intent(MyProfile.this,changetpin.class);
            f.putExtra("id",id);
            startActivity(f);
        }
        return super.onOptionsItemSelected(item);
    }
}

