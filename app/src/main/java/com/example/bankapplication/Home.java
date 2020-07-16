package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    SQLiteDatabase database;
    DatabaseHelper helper;
    private ImageView logout,money,profile,addmoney,passbook;
   private TextView tvbalance,txt;
    private  String id,name;
    private String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        helper=new DatabaseHelper(this);
        logout=findViewById(R.id.logoutbtn);
        profile=findViewById(R.id.accountbtn);
        money=findViewById(R.id.fundtransferbtn);
        passbook=findViewById(R.id.passbookbtn);
        addmoney=findViewById(R.id.addmoneybtn);
        database=helper.getWritableDatabase();
        txt=findViewById(R.id.nametxt);
        name=txt.getText().toString();
        tvbalance=findViewById(R.id.txtbalance);
       mobile = getIntent().getStringExtra("phone");
        Log.d("mobile",mobile);
      Cursor c=database.rawQuery("select id,fname,balance from customer where contact='"+mobile+"'",null);
        if(c!=null)
        {
            int balanceindex=c.getColumnIndex("balance");
           int nameindex=c.getColumnIndex("fname");
            c.moveToFirst();
            do {
               String dname=c.getString(nameindex);
                String dbalance=c.getString(balanceindex);
               id=c.getString(c.getColumnIndex("id"));

                txt.setText(name+" "+c.getString(c.getColumnIndex("fname"))+" "+"your balance is");
                tvbalance.setText(dbalance);

            }while (c.moveToNext());
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,MainActivity.class));
                finish();
            }
        });
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,FundTransfer.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,MyProfile.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Addmoney.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,Passbook.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
    }

   @Override
    public void onStart()
   {
       super.onStart();
       Cursor c=database.rawQuery("select id,fname,balance from customer where contact='"+mobile+"'",null);
       if(c!=null) {
           int balanceindex = c.getColumnIndex("balance");
           int nameindex = c.getColumnIndex("fname");
           c.moveToFirst();
           do {
               String dname = c.getString(nameindex);
               String dbalance = c.getString(balanceindex);
               id = c.getString(c.getColumnIndex("id"));
               tvbalance.setText(dbalance);
               txt.setText(name+" "+c.getString(c.getColumnIndex("fname"))+" "+"your balance is");
           } while (c.moveToNext());
       }
   }
   @Override
    public void onResume()
   {
       super.onResume();
       Cursor c=database.rawQuery("select id,fname,balance from customer where contact='"+mobile+"'",null);
       if(c!=null) {
           int balanceindex = c.getColumnIndex("balance");
           int nameindex = c.getColumnIndex("fname");
           c.moveToFirst();
           do {
               String dname = c.getString(nameindex);
               String dbalance = c.getString(balanceindex);
               id = c.getString(c.getColumnIndex("id"));
               tvbalance.setText(dbalance);
               txt.setText(name+" "+c.getString(c.getColumnIndex("fname"))+" "+"your balance is");
           } while (c.moveToNext());
       }
   }
}
