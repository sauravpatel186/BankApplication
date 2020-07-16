package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Addmoney extends AppCompatActivity implements View.OnClickListener {

    private EditText newamount;
    private TextView balance;
    private Button btn;
    private SQLiteDatabase database;
    private String id,bal;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);
newamount=findViewById(R.id.anewamount);
        balance=findViewById(R.id.abalance);
        bal=balance.getText().toString();
        btn=findViewById(R.id.addbtn);
    btn.setOnClickListener(this);
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        database=databaseHelper.getWritableDatabase();

        id=getIntent().getStringExtra("id");

        Cursor c=database.rawQuery("select balance from customer where id='"+id+"'",null);
        if(c!=null)
        {
            c.moveToFirst();
            do {
                amount=c.getDouble(c.getColumnIndex("balance"));
                balance.setText(bal+" "+(c.getString(c.getColumnIndex("balance"))));

            }while (c.moveToNext());
        }


    }



    @Override
    public void onClick(View v) {
            if (newamount.getText().toString().equals(""))
            {
                newamount.setError("Cannot be Empty");
            } else {
                AlertDialog.Builder build = new AlertDialog.Builder(Addmoney.this);
                build.setMessage("Credit Card/Debit card details");
                LinearLayout ll = new LinearLayout(Addmoney.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                final EditText card_no = new EditText(Addmoney.this);
                card_no.setHint("card number");
                card_no.setInputType(InputType.TYPE_CLASS_NUMBER);
                final String carpattern = "[0-9]{16}";
                card_no.setError(null);

                final EditText expiry_date = new EditText(Addmoney.this);
                expiry_date.setHint("MM/YY");
                expiry_date.setInputType(InputType.TYPE_CLASS_DATETIME);
                expiry_date.setError(null);


                final EditText cvv = new EditText(Addmoney.this);
                cvv.setHint("ccv");
                cvv.setInputType(0x00000012);
                cvv.setError(null);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy");
                Format f = new SimpleDateFormat("MM/yy");
                final String strDate = f.format(new Date());

                final String cvvpattern = "[0-9]{3}";
                ll.addView(card_no);
                ll.addView(expiry_date);
                ll.addView(cvv);
                build.setView(ll);
                build.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = build.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean validate=true;
                        if (!(card_no.getText().toString().matches(carpattern))) {
                            card_no.setError("Not Valid card no.");
                            validate=false;
                            Log.d("card",card_no.getText().toString());
                        }
                        else if (card_no.getText().toString().equals("")) {
                            card_no.setError("Card no cannot be empty");
                            validate=false;
                        }
                        else if (expiry_date.getText().toString().equals(strDate)) {
                            expiry_date.setError("Not valid date");
                            validate=false;
                        }
                        else if (expiry_date.getText().toString().equals("")) {
                            expiry_date.setError("Expiry Date cannot be empty");
                            validate=false;
                        }
                        else if (!(cvv.getText().toString().matches(cvvpattern))) {
                            cvv.setError("Not Valid CVV");
                            validate=false;
                        }
                        else if (cvv.getText().toString().equals("")) {
                            cvv.setError("CVV cannot be empty");
                            validate=false;
                        }
                        else if(validate) {
                            double sum_amount = Double.parseDouble(newamount.getText().toString()) + amount;
                            database.execSQL("update customer set balance=" + sum_amount + " where id=" + id + "");
                            Intent i = new Intent(Addmoney.this, Addmoney.class);
                            i.putExtra("id", id);
                            startActivity(i);
                            finish();
                            newamount.setText("");

                            Toast.makeText(Addmoney.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

