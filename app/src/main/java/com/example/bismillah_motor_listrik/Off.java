package com.example.bismillah_motor_listrik;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Off extends AppCompatActivity {

    String billing_off, jarak_off;
    TextView billing, jarak;
    Button end;
    Handler handler;

    Runnable runnable;
    private View decorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off);

        billing = findViewById(R.id.tagihan);
        jarak = findViewById(R.id.jarak);

        decorView = getWindow() .getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        Bundle extras = getIntent().getExtras();
        billing_off = extras.getString("billing_off");
        jarak_off = extras.getString("jarak_off");

        billing.setText(billing_off);
        jarak.setText(jarak_off);

//        goMain();
        end = findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Off.this, BluetoothFragment.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }


//    private void goMain() {
//        handler = new Handler();
//        runnable = new Runnable() {
//            public void run() {
//                Intent i = new Intent(Off.this, BluetoothFragment.class);
//                startActivity(i);
//            }
//        };
//        handler.postDelayed(runnable, 60000);
//    }
    @Override
    protected void onStop() {
        super.onStop();
//        handler.removeCallbacks(runnable);
    }
    @Override
    public void onBackPressed () {

    }
}