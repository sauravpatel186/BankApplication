package com.example.bankapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
  
    public static final String DATABASE_NAME="bank_db";
    public static int VERSION=1;
    public  static final String query = "create table customer(email TEXT(20),name TEXT(25),phone NUMBER(12) PRIMARY KEY,password TEXT(15),dob TEXT(10),address TEXT(10))";
    public DatabaseHelper(@Nullable Context context) {
        super(context,"bankdatabase.db",null,1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String query = "create table customer(id INTEGER PRIMARY KEY AUTOINCREMENT,fname varchar(20),lname varchar(20),contact bigint,lpin int,tpin int,email varchar(50),gender varchar(10),accountno int,balance double)";

            db.execSQL(query);
            db.execSQL("create table photo(account int,pic blob )");
            db.execSQL("create table deposit (account int,amount double,duration varchar(30),date varchar(30))");
            db.execSQL("create table trans (from_account int,to_account int,amount double,date varchar(30))");
        }
     catch(Exception e)
            {
                Log.d("Error", e.toString());
                e.printStackTrace();
            }


    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
