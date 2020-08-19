package com.example.deltaonsitetask2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    EditText phoneno;
    EditText message;
    Button button;
    String messagevalue, phonenovalue;
    Integer minutes, hour;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission")
                        .setMessage("This permission needed for sending SMS(DUH!)")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "WHY DID YOU EVEN INSTALL THIS", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
            }
        }


        timePicker = findViewById(R.id.timepicker);
        phoneno = findViewById(R.id.Phone);
        message = findViewById(R.id.Message);
        button = findViewById(R.id.button);

        prefs = this.getSharedPreferences("STORAGE",
                Context.MODE_PRIVATE);
        editor = prefs.edit();

        messagevalue = prefs.getString("message", "");
        phonenovalue = prefs.getString("phone", "");
        minutes = prefs.getInt("minutes", -1);
        hour = prefs.getInt("hour", -1);
        if (hour != -1 && minutes != -1) {
            timePicker.setHour(hour);
            timePicker.setMinute(minutes);
            phoneno.setText(phonenovalue);
            message.setText(messagevalue);
        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minutes = i1;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagevalue = message.getText().toString();
                phonenovalue = phoneno.getText().toString();
                if (hour == -1 || minutes == -1 || messagevalue.equals("") || phonenovalue.equals(""))
                    Toast.makeText(MainActivity.this, "Fill all values", Toast.LENGTH_LONG).show();
                else {
                    editor.putString("message", messagevalue);
                    editor.putString("phone", phonenovalue);
                    editor.putInt("hour", hour);
                    editor.putInt("minutes", minutes);
                    editor.apply();
                    Intent serviceintent = new Intent(MainActivity.this, service.class);
                    startService(serviceintent);

                }
            }
        });


    }
}