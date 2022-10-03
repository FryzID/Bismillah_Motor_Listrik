package com.example.bismillah_motor_listrik;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Off extends AppCompatActivity {

    String billing_off, jarak_off;
    TextView billing, jarak;
    Button end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off);

        billing = findViewById(R.id.tagihan);
        jarak = findViewById(R.id.jarak);

        Bundle extras = getIntent().getExtras();
        billing_off = extras.getString(billing_off);
        jarak_off = extras.getString(jarak_off);

        billing.setText(billing_off);
        jarak.setText(jarak_off);

        goMain();
        end = findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Off.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void goMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Off.this, MainActivity.class);
                startActivity(i);
            }
        }, 60000);
    }
}