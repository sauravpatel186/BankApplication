package com.example.bankapplication;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.text.InputType;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import java.text.SimpleDateFormat;
        import java.util.Date;

public class FundTransfer extends AppCompatActivity implements View.OnClickListener {
    private EditText amount,acno,racno,rname;
    private String name;
    private SQLiteDatabase database;
    private Button verify,transfer,cancel;
    private String id,currentdatetime;
    private double amt,from_bal,to_bal;
    private   String anopattern="[0-9]{4}";
    private int verified_flag=0,d_tpin,from_ano,to_ano,i_accountno;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        amount=findViewById(R.id.edtamount);
        acno=findViewById(R.id.editacno);
        racno=findViewById(R.id.edtraco);
        rname=findViewById(R.id.edtrname);
        verify=findViewById(R.id.verifybtn);
        transfer=findViewById(R.id.transferbtn);
        cancel=findViewById(R.id.cancelbtn);

        id=getIntent().getStringExtra("id");
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        database=databaseHelper.getWritableDatabase();
    progressDialog=new ProgressDialog(FundTransfer.this);



        Cursor c=database.rawQuery("select accountno from customer where id='"+id+"'",null);
        if(c!=null)
        {
            c.moveToFirst();
            do {
                acno.setText((c.getString(c.getColumnIndex("accountno"))));
               from_ano=Integer.parseInt(acno.getText().toString());
                acno.setFocusable(false);
            }while (c.moveToNext());
        }
        verify.setOnClickListener(this);
        cancel.setOnClickListener(this);
        transfer.setOnClickListener(this);
        transfer.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.verifybtn)
        {
            if(racno.getText().toString().equals(""))
            {

              racno.setError("Cannot Be Empty");
            }
            else if(!(racno.getText().toString().matches(anopattern)))
            {
                racno.setError("Alphabet Not Allowed");
            }
            else {

                to_ano = Integer.parseInt(racno.getText().toString());
                Cursor c = database.rawQuery("select accountno,fname,lname,balance from customer where accountno='" + to_ano + "' ", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        i_accountno = Integer.parseInt(c.getString(c.getColumnIndex("accountno")));
                        if (to_ano == i_accountno) {
                            rname.setText((c.getString(c.getColumnIndex("fname"))) + " " + (c.getString(c.getColumnIndex("lname"))));
                            rname.setTextColor(Color.GREEN);
                            to_bal = c.getDouble(c.getColumnIndex("balance"));
                            verified_flag = 1;
                        }
                    } while (c.moveToNext());
                    transfer.setEnabled(true);
                } else {
                   Toast.makeText(FundTransfer.this,"No Such Account Found",Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(v.getId()==R.id.transferbtn)
        {
            SimpleDateFormat sd=new SimpleDateFormat("dd/mm/YYYY HH:mm:ss");

            currentdatetime=sd.getDateTimeInstance().format(new Date());
            Log.d("date",currentdatetime);
            amt=Double.parseDouble(amount.getText().toString());
            Cursor c=database.rawQuery("select tpin,balance from customer where id='"+id+"' ",null);
            if(c!=null)
            {
                c.moveToFirst();
                do {
                    from_bal=c.getLong(c.getColumnIndex("balance"));
                    d_tpin=c.getInt(c.getColumnIndex("tpin"));
                }while (c.moveToNext());
            }
            if (verified_flag==0)
            {
                Toast.makeText(FundTransfer.this,"Verified Account",Toast.LENGTH_SHORT).show();
            }
            else if(Double.parseDouble(amount.getText().toString())<100)
            {
                Toast.makeText(FundTransfer.this,"Add minimum100 Rupees",Toast.LENGTH_SHORT).show();
            }
            else if(Double.parseDouble(amount.getText().toString())>=from_bal)
            {
                Toast.makeText(FundTransfer.this,"Please check your balance ",Toast.LENGTH_SHORT).show();
            }
            else
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(FundTransfer.this);
                builder.setMessage("Enter Transaction Password");
                LinearLayout ll=new LinearLayout(FundTransfer.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                final EditText tpin=new EditText(FundTransfer.this);
                tpin.setInputType(0x00000012);
                ll.addView(tpin);
                tpin.setError(null);
                builder.setCancelable(true);
                builder.setView(ll);
                builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pinpattern="[0-9]{4}";
                        if(!(tpin.getText().toString().matches(pinpattern))) {
                            tpin.setError("Not Valid Pin");
                        }
                        else {
                            if (d_tpin == (Integer.parseInt(tpin.getText().toString()))) {
                                if (from_bal >= amt) {
                                    progressDialog.show();
                                    database.execSQL("insert into trans(from_account,to_account,amount,date) values (" + from_ano + "," + to_ano + "," + amt + ",'" + currentdatetime + "')");

                                    Double from_new_amount = from_bal - amt;
                                    database.execSQL("update customer set balance =" + from_new_amount + " where accountno=" + from_ano + "");

                                    Double to_new_amount = to_bal + amt;
                                    database.execSQL("update customer set balance=" + to_new_amount + " where accountno=" + to_ano + "");

                                    Toast.makeText(FundTransfer.this, "Successfully Transfer", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(FundTransfer.this, Home.class));
                                    finish();
                                }
                            }
                        }
                    }
                });
            }


        }
    }
}
