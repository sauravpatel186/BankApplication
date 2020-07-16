package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

public class Passbook extends AppCompatActivity {

    private TextView txt;
    private SQLiteDatabase database;
    private int id, accountno;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        txt = findViewById(R.id.poutput);
        String result = "";
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        DatabaseHelper helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        Cursor c = database.rawQuery("select accountno from customer where id=" + id + "", null);
        if (c != null) {
            c.moveToFirst();
            do {
                accountno = Integer.parseInt(c.getString(c.getColumnIndex("accountno")));
            } while (c.moveToNext());

        }
        try {
            try {


                Cursor c1 = database.rawQuery("select * from trans where to_account=" + accountno + " OR from_account=" + accountno + "", null);

                if (c1 != null) {
                    c1.moveToFirst();
                    do {
                        int from_accountno = c1.getInt(c1.getColumnIndex("from_account"));
                        int to_accountno = c1.getInt(c1.getColumnIndex("to_account"));
                        Double amount = c1.getDouble(c1.getColumnIndex("amount"));
                        String d = c1.getString(c1.getColumnIndex("date"));
                        if (to_accountno == accountno) {
                            result = result + d + "\t\t\t" + from_accountno + "\t\t\t\t\t\t\t\t\t" + amount + "\t\t\t\t\t\t" + "CR" + "\n\n\n";
                        } else if (from_accountno == accountno) {
                            result = result + d + "\t\t\t" + to_accountno + "\t\t\t\t\t\t\t\t\t" + amount + "\t\t\t\t\t\t" + "DR" + "\n\n\n";
                        }
                    } while (c1.moveToNext());
                }
            }catch(CursorIndexOutOfBoundsException e)
            {
                Toast.makeText(Passbook.this,"No Transaction History Found",Toast.LENGTH_SHORT).show();

            }


        }catch (NullPointerException s)
        {
            Toast.makeText(Passbook.this,"No Transaction History Found",Toast.LENGTH_SHORT).show();
        }
        txt.setText(result);
    }
}
