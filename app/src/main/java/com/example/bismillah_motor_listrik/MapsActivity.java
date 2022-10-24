package com.example.bismillah_motor_listrik;

import static android.content.ContentValues.TAG;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bismillah_motor_listrik.API.InterfaceAPI;
import com.example.bismillah_motor_listrik.model.Motor;
import com.example.bismillah_motor_listrik.model.PostMotor;
import com.example.bismillah_motor_listrik.model.Respon;
import com.example.bismillah_motor_listrik.model.Update;
import com.example.bismillah_motor_listrik.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bismillah_motor_listrik.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterInputStream;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {


    BroadcastReceiver batteryBroadcast;
    IntentFilter intentFilter;
    TextView tv_speed;
    boolean status_alert;
    String kecepatan_realtime, jarak_realtime, battery_realtime, motor_id;
    TextView tagihan, jarak, kacau;
    String id, crd;
    Integer nilai, nl, tampil;
    Handler handler1, handler3;
    Runnable runnable1, runnable2, runnable3;
    private String KEY_NAME = "NAMA";
    Dialog myDialog;
    private Button buttonpopupstnby, buttonPopUpOff;
    private View decorView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Button btn, btn_off;
    private Handler handler; // handler that gets info from Bluetooth service

    public static final String DEVICE_EXTRA = "com.example.bismillah_motor_listrik.SOCKET";
    public static final String DEVICE_UUID = "com.example.bismillah_motor_listrik.uuid";
    private static final String DEVICE_LIST = "com.example.bismillah_motor_listrik.devicelist";
    private static final String DEVICE_LIST_SELECTED = "com.example.bismillah_motor_listrik.devicelistselected";
    public static final String BUFFER_SIZE = "com.example.bismillah_motor_listrik.buffersize";
    private static final String TAG = "BlueTest5-MainActivity";
    private BluetoothAdapter mBTAdapter;
    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
    private static final int SETTINGS = 20;
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int mBufferSize = 50000; //Default
    private ReadInput mReadThread = null;

    private int mMaxChars = 50000;//Default//change this to string..........
    private BluetoothSocket mBTSocket;

    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;

    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;

    private Chronometer chronometer;

    private boolean running;

    final Context context = this;

    TextView Coundown;

    private BluetoothDevice mDevice;

    private static final String FORMAT = "%02d:%02d:%02d";

    int seconds , minutes, speed;

    //TODO Bluetooth service


    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;


    //Declare timer
    CountDownTimer cTimer = null;
    private int level;

    TextView textViewTime, saldo;
    String key_device, key_mBuffer, key_mDevice;

    private ProgressDialog progressDialog;
//    count up time
    TextView timerText, mTxtReceive, battery;

    Timer timer;
    TimerTask timerTask;

    Double times = 0.0;
    private InputStream inputStream;

//    alert
    Handler handler_alert, handler_alert2;
    Runnable runnable_alert, runnable_alert2;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTxtReceive = (TextView) findViewById(R.id.percobaan);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        //TODO HP Battery Service
//        this.registerReceiver(this.mBatInfoReceive, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        myDialog = new Dialog(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(Scanner.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(Scanner.DEVICE_UUID));
        mMaxChars = b.getInt(Scanner.BUFFER_SIZE);

        if (mBluetoothAdapter == null) {
            out.append("device not supported");
        }

        Log.d(TAG, "Ready");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);

        battery = (TextView) findViewById(R.id.baterry);

        status_alert = false;

//        Credit
        tagihan = findViewById(R.id.tagihan);
        jarak = findViewById(R.id.jarak);
//        ambil id
        Bundle extras = getIntent().getExtras();
        id = extras.getString(KEY_NAME);

//        try {
            key_device = extras.getString(key_device);
            key_mDevice = extras.getString(key_mDevice);
            key_mBuffer = extras.getString(key_mBuffer);
            saldo = findViewById(R.id.saldo);
//        } catch (Exception e) {
//            motorOff();
//            Off();
//            habis();
//        }

        saldo.setText(key_device);
//      definisikan nilai
//        kecepatan_realtime = "0";
//        jarak_realtime = "0";
//        battery_realtime = "0";
//        motor_id = "0";
//        Function billing
        credit();
//        loopCredit();
//        jarak.setText(jarak_realtime);
//        nl = 10;
        tampil = 1;
        if (tampil != 0) {
            setCredit();
        }

//        count time
        timerText = findViewById(R.id.timerText);
        timer = new Timer();
        startTimer();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        btn_off = findViewById(R.id.btn_off);
        buttonPopUpOff = findViewById(R.id.btn_off);

        decorView = getWindow() .getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        buttonpopupstnby = (Button) findViewById(R.id.btn_stnby);
        buttonpopupstnby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                count up time
                timerTask.cancel();
                // create a Dialog component
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                //tell the Dialog to use the dialog.xml as it's layout description
                dialog.setContentView(R.layout.activity_standbypopupp);
//                dialog.setTitle("Android Custom Dialog Box");
                dialog.setCancelable(false);

                Standby();
                motorStnBy();
                TextView ada = (TextView) dialog.findViewById(R.id.countdown);
//                TextView txt = (TextView) dialog.findViewById(R.id.txt);
                new CountDownTimer(300000, 1000) {

                    public void onTick(long millisUntilFinished) {
//                        ada.setText("" + millisUntilFinished / 1000);
                        ada.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        ada.setText("done!");
                        motorOff();
                        Off();
                        habis();
                        return;
                    }
                }.start();

//                txt.setText("This is an Android custom Dialog Box Example! Enjoy!");

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_resume);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startTimer();
                        Resume();
                        motorOn();

                    }
                });

                dialog.show();
            }
        });

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

//        CheckBluetoothState();
        BluetoothStart();

        loopRealtime();

//        BatteryTrigger();





        textViewTime = (TextView)findViewById(R.id.countdown);

        tv_speed = findViewById(R.id.tv_speed);

        intentFilterAndBroadcast();
    }

    private void intentFilterAndBroadcast() {

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
//
//                    Integer lvl = Integer.valueOf(intent.getIntExtra("level", 0 ));
//                    if (lvl <= 50){
//                        chargePhone();
//                    } else if (lvl == 100){
//                        chargeDone();
//                    }
//                }
            }
        };

    }


    private void Standby() {
        handler1.removeCallbacks(runnable1);

//        onStop()
    }


    private void Resume() {
//        btn.setVisibility(View.VISIBLE);
//        btn_resume.setVisibility(View.INVISIBLE);
        if (tampil != 0) {
            setCredit();
        }
        onStart();
//        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer); // initiate a chronometer
//        simpleChronometer.start(); // start a chronometer
    }

    private void Off() {
//        handler1.removeCallbacks(runnable1);
//        handler3.removeCallbacks(runnable3);
//        handler_alert.removeCallbacks(runnable_alert);
//        handler_alert2.removeCallbacks(runnable_alert2);
        onStop();
        stop();
        timerTask.cancel();
        times = 0.0;
        timerText.setText(formatTime(0,0,0));

        unregisterReceiver(batteryBroadcast);

        //TODO REMOVING INTENT
        getIntent().removeExtra("mDevice");
        getIntent().removeExtra("mDeviceUUID");
        getIntent().removeExtra("mMaxChars");
        getIntent().removeExtra("id");

        getIntent().replaceExtras(new Bundle());
        getIntent().setAction("");
        getIntent().setData(null);
        getIntent().setFlags(0);

//        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer); // initiate a chronometer
//
//        simpleChronometer.start(); // start a chronometer

    }

    private void stop() {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfaceAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        String tgh = tagihan.getText().toString();

        InterfaceAPI api = retrofit.create(InterfaceAPI.class);

        Update put = new Update(tgh, "1", "1");

        Call<Respon> call = api.putCredit(id, put);

        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                if (!response.isSuccessful()) {
                    return;
                }
//                Toast.makeText(MapsActivity.this, "Berhasil Put", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
//                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void habis() {
        Intent i = new Intent(MapsActivity.this, Off.class);
        i.putExtra("billing_off", tagihan.getText().toString());
        i.putExtra("jarak_off", jarak.getText().toString());
        startActivity(i);
    }

    private void setCredit() {
        handler1 = new Handler();
        runnable1 = new Runnable() {
            public void run() {
                try {
                    if(tagihan.getText().toString() == "") {
                        credit();
                    }
                    String jarak1 = jarak.getText().toString();
//                    Toast.makeText(MapsActivity.this, jarak_realtime, Toast.LENGTH_SHORT).show();
                    int nl = Integer.parseInt(jarak1);
                    nilai = (nl / 10) * 250;
                    int trans = Integer.parseInt(crd);
                    tampil = trans - nilai;
                    if (tampil <= 0) {
                        Toast.makeText(MapsActivity.this, "Billing Telah Habis", Toast.LENGTH_SHORT).show();
                        motorOff();
                        Off();
                        habis();
                        return;
                    }
                    if (tampil <= 10000) {
                        Toast.makeText(MapsActivity.this, "Billing Tersisa 10.0000", Toast.LENGTH_SHORT).show();
                    }

                    tagihan.setText(tampil.toString());

                    setCredit();
                }catch (Exception e) {
//                    Toast.makeText(MapsActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                    setCredit();
                }

            }
        };
        handler1.postDelayed(runnable1, 5000); // 15000 detik sebelumnya

//        stop_Standby.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.removeCallbacks(runnable1);
//            }
//        });
    }

//    private void jarak() {
//        handler2 = new Handler();
//        runnable2 = new Runnable() {
//            public void run() {
//                nl++;
//                jarak.setText(nl.toString());
//                jarak();
//            }
//        };
//        handler2.postDelayed(runnable2, 1000);
//        stop_Standby.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handler.removeCallbacks(runnable2);
//            }
//        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void  run() {
//                nl++;
//                jarak.setText(nl.toString());
//                jarak();
//            }
//        }, 1000);
//    }

    private void credit() {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfaceAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        InterfaceAPI api = retrofit.create(InterfaceAPI.class);

        Call<User> call = api.user(id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                tagihan.setText(response.body().getData().getCredit().toString());
                TextView saldo = (TextView) findViewById(R.id.saldo);
                saldo.setText(response.body().getData().getCredit().toString());
                crd = tagihan.getText().toString();
                int trans = Integer.parseInt(crd);
                Integer pajak = trans - 1000;
                tagihan.setText(pajak.toString());
                crd = tagihan.getText().toString();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void openstandbypopup(){
//        Intent intent = new Intent(this, standbypopupp.class);
//        startActivity(intent);
//    }

    public void ShowPopup (View v){
        myDialog.setContentView(R.layout.activity_standbypopupp);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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

    //TODO BLUETOOTH
    @SuppressLint("MissingPermission")
    private void BluetoothStart() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Perangkat Tidak Mendukung Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }

    }

    //TODO Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                @SuppressLint("MissingPermission") String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        // Don't forget to unregister the ACTION_FOUND receiver.

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ENABLE_BT) {
//            CheckBluetoothState();
//        }
//    }

//    @SuppressLint("MissingPermission")
//    private void CheckBluetoothState() {
//        // Checks for the Bluetooth support and then makes sure it is turned on
//        // If it isn't turned on, request to turn it on
//        // List paired devices
//        if(btAdapter==null) {
//
//            return;
//        } else {
//            if (btAdapter.isEnabled()) {
////                textview1.append("\nBluetooth is enabled...");
//
//                // Listing paired devices
////                textview1.append("\nPaired Devices are:");
//                @SuppressLint("MissingPermission") Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
//                for (BluetoothDevice device : devices) {
////                    textview1.append("\n  Device: " + device.getName() + ", " + device);
//                }
//            } else {
//                //Prompt user to turn on Bluetooth
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//        }
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnMapLongClickListener( this);
//        mMap.setOnMarkerDragListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            enableUserLocation();
//                    zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Dialog Why Permission Important
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_REQUEST_CODE);
            }
        }
        // Add a marker in Sydney and move the camera
//        LatLng latLng = new LatLng(27.231,13.3123);
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("IDK").snippet("wonder");
//        mMap.addMarker(markerOptions);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//        mMap.animateCamera(cameraUpdate);

//        try {
//            List<Address> addresses = geocoder.getFromLocationName("abc.xyz", 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                LatLng london = new LatLng(address.getLatitude(), address.getLongitude());
//
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(new LatLng(address.getLatitude(), address.getLongitude()))
//                        .title(address.getLocality());
//                mMap.addMarker(markerOptions);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, 16));
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    public void stringLocation(Location location) {


    }

    private void loopRealtime() {
        handler3 = new Handler();
        runnable3 = new Runnable() {
            public void run() {
                realtime();
                loopRealtime();
//                BatteryTrigger();
            }
        };
        handler3.postDelayed(runnable3, 2500);
        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerTask.cancel();
                Standby();
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Berhenti");
                builder.setMessage("Apakah Anda Yakin Untuk Berhenti?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        motorOff();
                        Off();
                        habis();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startTimer();
                        Resume();
                        dialogInterface.cancel();

                    }
                });

                builder.show();
            }
        });
//        btn_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//
//                builder.setCancelable(true);
//                builder.setTitle("Berhenti");
//                builder.setMessage("Apakah Anda Yakin Untuk Berhenti?");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        builder.show();
//                        handler.removeCallbacks(runnable3);
//                        Off();
//                        habis();
//                        motorOff();
//                        return;
//
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//
//                    }
//                });
//            }
//        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                realtime();
//                loopRealtime();
//            }
//        }, 10000);
    }

    public void realtime() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//        String string_latlng = center.toString();


        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfaceAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        String btr = battery.getText().toString();
        String battery = btr;
        String jrk = jarak.getText().toString();
        String jarak =  jrk;

        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());

//        String latitude = "7.8652";
//        String longitude = "-73.9987";
//        String latitude = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS);
//        String longitude = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS);

        InterfaceAPI api = retrofit.create(InterfaceAPI.class);

        Motor realtime = new Motor(battery, jarak, latitude, longitude, id);

        Call<PostMotor> call = api.putMotor(motor_id, realtime);

        call.enqueue(new Callback<PostMotor>() {
            @Override
            public void onResponse(Call<PostMotor> call, Response<PostMotor> response) {
//                try {
                    if (!response.isSuccessful()) {
//                        Toast.makeText(MapsActivity.this, "Not Succes", Toast.LENGTH_SHORT).show();
//                        motorOff();
//                        Off();
//                        habis();
                        return;
                    }

//                    Toast.makeText(MapsActivity.this, "Berhasil Realtime", Toast.LENGTH_SHORT).show();

                    String rg = response.body().getRange().toString();
//                    String btr11 = response.body().getBattery().toString();
                    int trans = Integer.parseInt(rg);
//                    int btr1 = Integer.parseInt(btr11);

                    if (response.body().getOff() != null) {
                        motorOff();
                        Off();
                        habis();
                        return;
                    }
//
//                    if (btr1 == 2) {
//                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MapsActivity.this);
//
//                        builder.setCancelable(true);
//                        builder.setTitle("Perhatian !!");
//                        builder.setMessage("Battery Motor Tersisa kurang dari 50 persen");
//
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//
//                        builder.show();
//
//                    } else if(btr1 == 1) {
//                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MapsActivity.this);
//
//                        builder.setCancelable(true);
//                        builder.setTitle("Perhatian !!");
//                        builder.setMessage("Battery Motor Hampir Habis, Mohon Kembali Ke post terdekat");
//
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//
//                        builder.show();
//                    }
//
//                    if(trans == 1) {
//                        alert2();
//                    } else {
//
//                        if(status_alert == true) {
//                            handler_alert.removeCallbacks(runnable_alert);
//                            handler_alert2.removeCallbacks(runnable_alert2);
//                            status_alert = false;
//                        } else {
//
//                        }
//
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(MapsActivity.this, "gagal realtime", Toast.LENGTH_SHORT).show();
//                }


            }

            @Override
            public void onFailure(Call<PostMotor> call, Throwable t) {
//                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    alert
private void  alert2() {
    handler_alert2 = new Handler();
    runnable_alert2 = new Runnable() {
        public void run() {
            alert();
            alert2();
        }
    };
    handler_alert2.postDelayed(runnable_alert2, 5000);
}

    private void alert() {
        status_alert = true;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MapsActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Perhatian !!");
        builder.setMessage("Mohon Kembali Ke Zona Yang Ada");

        handler_alert = new Handler();
        runnable_alert = new Runnable() {
            public void run() {

                motorOff();
                Off();
                handler_alert.removeCallbacks(runnable_alert);
                handler_alert2.removeCallbacks(runnable_alert2);
                habis();
                return;
            }
        };
        handler_alert.postDelayed(runnable_alert, 5000);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
//    end alert

    private void setUserLocationMarker(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        if (userLocationMarker == null) {
            //Create New Marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.motor_01));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//            speed = (int) (location.getSpeed() * 18 / 5);
//            TextView tv = (TextView) findViewById(R.id.tv_speed);
//            tv.setText((int) speed);
        } else {
            //Use Previously created Marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(0, 0, 0, 0));
            circleOptions.fillColor(Color.argb(0, 0, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(batteryBroadcast, intentFilter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            //NEED REQUEST PERMISSION
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
        stopLocationUpdates();
        handler1.removeCallbacks(runnable1);
        handler3.removeCallbacks(runnable3);
        motorOff();

    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

//    @Override
//    public void onMapLongClick(@NonNull LatLng latLng) {
//        Log.d(TAG, "onMapLongClick: " + latLng.toString());
//        try {
//            List<Address> addresses =  geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                String streetAddress = address.getAddressLine(0);
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title(streetAddress)
//                        .draggable(true)
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    @Override
//    public void onMarkerDrag(@NonNull Marker marker) {
//        Log.d(TAG, "onMarkerDrag: ");
//    }

//    @Override
//    public void onMarkerDragEnd(@NonNull Marker marker) {
//        Log.d(TAG, "onMarkerDragEnd: ");
//        LatLng latLng = marker.getPosition();
//        try {
//            List<Address> addresses =  geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                String streetAddress = address.getAddressLine(0);
//                marker.setTitle(streetAddress);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onMarkerDragStart(@NonNull Marker marker) {
//        Log.d(TAG, "onMarkerDragStart: ");
//    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
//                doStuff();
            } else {
                //Showing Dialog That permission is not granted...
                finish();
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {


    }


    /**
     * Called when the user touches the button
     */
//    public void standBy(View view) {
//        stopLocationUpdates();
//        startTimer();
//        btn.setText("Resume");
//
//    }

    //start timer function
//    void startTimer() {
//        countdown = (TextView) findViewById(R.id.countdown);
//        cTimer = new CountDownTimer(300000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                countdown.setText("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//            }
//        };
//        cTimer.start();
//    }
//
//    //cancel timer
//    void cancelTimer() {
//        if (cTimer != null)
//            cTimer.cancel();
//    }


    // Defines several constants used when transmitting messages between the
    // service and the UI.
    public interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);

                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();


                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

//    private BroadcastReceiver mBatInfoReceive = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
////            TextView battery = (TextView) findViewById(R.id.baterry);
//            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//
//            String str = new String(String.valueOf(level));
//
//            int batu = Integer.parseInt(str);
////            battery.setText(str);
//
//            if (batu <= 50) {
//                chargePhone();
//            }
//
//            if (batu == 100) {
//
//                chargeDone();
//            }
//
//
//        }
//    };

    public void chargePhone(){

//        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
        try {
            //TODO Battery HP Charge
            String sendtxt = "2";
            mBTSocket.getOutputStream().write(sendtxt.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void chargeDone () {

//        Toast.makeText(this, "TEST2", Toast.LENGTH_SHORT).show();
        try {
            //TODO Battery HP Charge
            String sendtxt = "9";
            mBTSocket.getOutputStream().write(sendtxt.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



//    public void startTimer(){
//        int hoursToGo = 0;
//        int minutesToGo = 0;
//        int secondsToGo = 10;
//
//        int millisToGo = secondsToGo*1000+minutesToGo*1000*60+hoursToGo*1000*60*60;
//
//        new CountDownTimer(millisToGo,1000) {
//
//            @Override
//            public void onTick(long millis) {
//                int seconds = (int) (millis / 1000) % 60 ;
//                int minutes = (int) ((millis / (1000*60)) % 60);
//                String text = String.format("%02d:%02d",minutes,seconds);
//                tv.setText(text);
//            }
//
//            @Override
//            public void onFinish() {
//                tv.setText("Request Timeout");
//            }
//        }.start();
//
//        final AlertDialog d = (AlertDialog) getDialog();
//        final Timer timer2 = new Timer();
//        timer2.schedule(new TimerTask() {
//            public void run() {
//                d.dismiss();
//                timer2.cancel(); //this will cancel the timer of the system
//
//                Intent i =  new Intent(getActivity(), PromoActivity.class);
//                i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                getActivity().startActivityForResult(i,0);
//            }
//        }, 10000);
//    }

    private void motorOn() {

        ByteArrayOutputStream stream
                = new ByteArrayOutputStream();

        // Initializing string
//        String st = "0";

        // writing the specified byte to the output stream
        try {
            String sendtxt = "1";
            mBTSocket.getOutputStream().write(sendtxt.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // converting stream to byte array
        // and typecasting into string
//        String finalString
//                = new String(stream.toByteArray());
//
//        // printing the final string
//        System.out.println(finalString);

    }
    private void motorStnBy() {

        ByteArrayOutputStream stream
                = new ByteArrayOutputStream();

        // Initializing string
//        String st = "0";

        // writing the specified byte to the output stream
        try {
            String sendtxt = "0";
            mBTSocket.getOutputStream().write(sendtxt.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void motorOff() {

        ByteArrayOutputStream stream
                = new ByteArrayOutputStream();

        // Initializing string
//        String st = "0";

        // writing the specified byte to the output stream
        try {
            String sendtxt = "8";
            mBTSocket.getOutputStream().write(sendtxt.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        // converting stream to byte array
        // and typecasting into string
//        String finalString
//                = new String(stream.toByteArray());
//
//        // printing the final string
//        System.out.println(finalString);

    }



    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(MapsActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554

        }

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
// Unable to connect to device`
                // e.printStackTrace();
                mConnectSuccessful = false;



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device.Please turn on your Hardware", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();

        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
                    InputStream inputStream;

                    try {
                        inputStream = mBTSocket.getInputStream();

//                int jarak;
                        while (!bStop) {
                            byte[] buffer = new byte[2048];
                            if (inputStream.available() > 0) {
                                inputStream.read(buffer);
                                int i = 0;
                                String dataRead= new String(buffer,"UTF-8");

                                StringTokenizer tokens = new StringTokenizer(dataRead, ";");
                                tokens.countTokens();
                                String first = tokens.nextToken();      // this will contain ID MOTOR
                                String second = tokens.nextToken();     // this will contain Kecepatan
                                String third = tokens.nextToken();      // this will contain Jarak
                                String fourth = tokens.nextToken();      // this will contain Battery
                                String five = tokens.nextToken();      // this will contain Suhu

                                motor_id = String.valueOf(first);
                                kecepatan_realtime = String.valueOf(second);
                                jarak_realtime = String.valueOf(third);
                                battery_realtime = String.valueOf(fourth);
                                String suhu2 = String.valueOf(five);

                                TextView speed = (TextView) findViewById(R.id.tv_speed);
                                TextView jarak = (TextView) findViewById(R.id.jarak);
                                TextView battery = (TextView) findViewById(R.id.baterry);
                                TextView suhu = (TextView) findViewById(R.id.suhu);
                                System.err.println(dataRead);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //This code needs to be posted back to the main thread.
                                        speed.setText(kecepatan_realtime);
                                        jarak.setText(jarak_realtime);
                                        battery.setText(battery_realtime);
                                        suhu.setText(suhu2);


                                    }
                                });


                            }
                            Thread.sleep(1000);
                        }
                    } catch (IOException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }


        }

        public void stop() {
            bStop = true;
        }

    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {//cant inderstand these dotss

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }


    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
// TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        times++;
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(times);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }

    public void manageConnectedSocket(BluetoothSocket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            while(inputStream.available() == 0) {
                inputStream = socket.getInputStream();
            }
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes, 0, available);
            String ada = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(ada);

//            TextView percobaan = (TextView) findViewById(R.id.percobaan);
//            percobaan.setText(ada.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed () {

    }
}