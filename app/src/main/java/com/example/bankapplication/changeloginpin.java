package com.example.bankapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import static com.example.bankapplication.App.CHANNEL_1_ID;

public class changeloginpin extends AppCompatActivity {


    private EditText opin, npin, rpin, otp;
    private Button changepin, submit;
    private int id, oldpin, newpin, d_lpin, tempotp;
    private SQLiteDatabase database;
    private String tempid;
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelpin);
        tempid=getIntent().getStringExtra("id");
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        opin = findViewById(R.id.edtoldpin);
        notificationManager = NotificationManagerCompat.from(this);
        npin = findViewById(R.id.edtnewpin);
        rpin = findViewById(R.id.edtcpin);
        otp = findViewById(R.id.edtotp);
        otp.setEnabled(false);
        changepin = findViewById(R.id.changepinbtn);
        submit = findViewById(R.id.submitbtn);
        submit.setEnabled(false);
        final DatabaseHelper helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();
        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = database.rawQuery("select lpin from customer where id=" + id + "", null);
                if (c != null) {
                    c.moveToFirst();
                    do {
                        d_lpin = c.getInt(0);
                    } while (c.moveToNext());
                    oldpin = Integer.parseInt(opin.getText().toString());

                    if (oldpin == d_lpin && npin.getText().toString().matches("[0-9]{4}") && npin.getText().toString().equals(rpin.getText().toString())) {
                        addNotification();
                        otp.setEnabled(true);
                        submit.setEnabled(true);
                    } else {
                        Toast.makeText(changeloginpin.this, "Pin is not Correct", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type_otp=Integer.parseInt(otp.getText().toString());
                if(type_otp==tempotp)
                {
                    int i_newpin=Integer.parseInt(npin.getText().toString());
                    String query1="update customer set lpin="+i_newpin+" where id="+id+"";
                    database.execSQL(query1);
                    Toast.makeText(changeloginpin.this, "Pin Changed Successfully", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(changeloginpin.this,MyProfile.class);
                    i.putExtra("id", tempid);

                    startActivity(i);
                }
                else
                {
                    Toast.makeText(changeloginpin.this, "Incorrect OTP", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void addNotification() {
        Random random=new Random();
        tempotp=random.nextInt(999999-100001)+100001;

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("OTP")
                .setContentText("Your OTP Is "+tempotp)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
}
