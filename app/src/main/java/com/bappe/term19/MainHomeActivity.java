package com.bappe.term19;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainHomeActivity extends AppCompatActivity {
    // widget
    private TextView tv_roll;
    public ToggleButton tb;
    public ListView listview;
    public Button updateBtn;
    public ImageView imageView;
    public TextView status;

    // id info
    public String id;

    /*Used for Accelometer & Gyroscoper*/
    private SensorManager mSensorManager = null;
    private UserSensorListener userSensorListner;
    private Sensor mGyroscopeSensor = null;
    private Sensor mAccelerometer = null;

    /*Sensor variables*/
    private float[] mGyroValues = new float[3];
    private float[] mAccValues = new float[3];
    private double mAccPitch, mAccRoll;

    /*for unsing complementary fliter*/
    private float a = 0.2f;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private double pitch = 0, roll = 0;
    private double timestamp;
    private double dt;
    private double temp;
    private boolean running;
    private boolean gyroRunning;
    private boolean accRunning;

    // SharedPreference for user auto login
    public SharedPreferences sh_Score;
    public SharedPreferences.Editor toEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // get widget data
        imageView = findViewById(R.id.image1);
        status = findViewById(R.id.phone_statues);
        id = getIntent().getStringExtra("ID");
        listview = findViewById(R.id.listview);
        updateBtn = findViewById(R.id.updateBtn);
        tb = findViewById(R.id.toggleBtn);
        tv_roll = findViewById(R.id.tv_roll);

        // Tab 2 --> set ranking system
        setRanking();

        // Tab 1 --> implement sensor system (gyro & accelerator
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        userSensorListner = new UserSensorListener();
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Define Tab View
        TabHost tabHost1 = findViewById(R.id.tabHost1);
        tabHost1.setup();

        // First Tab. (Tab display text: "TAB 1"), (page view: "content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("Data");
        tabHost1.addTab(ts1);

        // Second Tab. (Tab display text: "TAB 2"), (page view: "content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("Rank");
        tabHost1.addTab(ts2);

        // toggle button
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tb.isChecked()) {
                    // execute toggle button --> active

                    // activate sensor
                    mSensorManager.registerListener(userSensorListner, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
                    mSensorManager.registerListener(userSensorListner, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

                    Toast.makeText(getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();

                    // activate thread
                    ServiceThread.startForever();
                    FirebasePost.isActive = true;

                    // toss the id info to service
                    Intent intent = new Intent(MainHomeActivity.this, MyService.class);
                    intent.putExtra("ID", id);

                    // start service
                    startService(intent);
                    // button image change
                    tb.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.roundbtn1)
                    );
                } else {
                    // inactive sensor
                    mSensorManager.unregisterListener(userSensorListner);
                    FirebasePost.isActive = false;
                    // finish the thread
                    ServiceThread.stopForever();

                    // stop service
                    Intent intent = new Intent(MainHomeActivity.this, MyService.class);
                    stopService(intent);


                    // button image change
                    tb.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.roundbtn)
                    );
                }
            }
        });

        // Update button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh_Score = getSharedPreferences("Score", MODE_PRIVATE);
                FirebasePost.getUserData();

                int currentScore;

                // get score information from Firebase Realtime DB
                if (sh_Score != null && sh_Score.contains("score")) {
                    currentScore = sh_Score.getInt("score", 0);
                }else{
                    currentScore = 0;
                }

                // update score info in Firebase Realtime DB
                FirebasePost.writeNewPost(id, FirebasePost.userScores.get(id) + currentScore);

                // init sharedPreference of score
                toEdit = sh_Score.edit();
                toEdit.putInt("score", 0);
                toEdit.commit();

                // update ranking ( For safety ranking update, duplicate)
                setRanking();
                setRanking();
            }
        });
    }

    @Override
    protected void onUserLeaveHint(){

        super.onUserLeaveHint();
    }

    // update ranking
    public void setRanking() {

        // get user ddata
        FirebasePost.getUserData();

        Iterator it = sortByValue(FirebasePost.userScores).iterator(); // sort by value

        // ArrayAdapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1) {
            // set text color of listview
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#00D85A"));
                return view;
            }
        };

        // get id and score
        while (it.hasNext()) {
            String dataID = (String) it.next();
            Log.d("Message2", "RANK " + dataID + " = " + FirebasePost.userScores.get(dataID));
            String rankData = dataID + " " + FirebasePost.userScores.get(dataID);
            adapter.add(rankData);
        }

        // set data to list
        listview.setAdapter(adapter);
    }



    private void complementaty(double new_ts) {

        /* Gyro and acceleration off */
        gyroRunning = false;
        accRunning = false;

        /*Since the first output of sensor value has error in dt (= timestamp - event.timestamp), break  */
        if (timestamp == 0) {
            timestamp = new_ts;
            return;
        }
        dt = (new_ts - timestamp) * NS2S; //  ns-> s conversion
        timestamp = new_ts;

        /* degree measure for accelerometer */
        mAccPitch = -Math.atan2(mAccValues[0], mAccValues[2]) * 180.0 / Math.PI; // Based on Y axis
        mAccRoll = Math.atan2(mAccValues[1], mAccValues[2]) * 180.0 / Math.PI; // Based on X axis

        /*
         * 1st complementary filter.
         *  mGyroValuess : Angular velocity component..
         *  mAccPitch : The angle of rotation obtained through the accelerometer..
         */
        temp = (1 / a) * (mAccPitch - pitch) + mGyroValues[1];
        pitch = pitch + (temp * dt);

        temp = (1 / a) * (mAccRoll - roll) + mGyroValues[0];
        roll = roll + (temp * dt);

        // print the angle
        tv_roll.setText("" + (int) roll + "°");
        status.setText("Running");

        // dynamically image change through neck tracking
        // bad --> bad image
        // good --> good image
        if (roll < 60) {
            imageView.setImageResource(R.drawable.bad_posture);
        }
        else{
            imageView.setImageResource(R.drawable.right_posture);
        }

        if(roll>-2 && roll<2) status.setText("\"Flatten\"");
        else status.setText("\"Running\"");

    }

    // Sensor Event function
    public class UserSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {

                /* GYROSCOPE */
                case Sensor.TYPE_GYROSCOPE:

                    /* Save sensor value to mGyroValues */
                    mGyroValues = event.values;

                    if (!gyroRunning)
                        gyroRunning = true;

                    break;

                /* ACCELEROMETER */
                case Sensor.TYPE_ACCELEROMETER:

                    /*Store sensor values in mAccValues*/
                    mAccValues = event.values;

                    if (!accRunning)
                        accRunning = true;

                    break;

            }

            //When the two sensors receive the new value, apply the complementary filter
            if (gyroRunning && accRunning) {
                complementaty(event.timestamp);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


    // when back pressed, finish the activity
    public void onBackPressed() {
        finish();
    }


    // sort function by value (Overriding)
    public static List sortByValue(final Map map) {

        List<String> list = new ArrayList();

        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });
        return list;
    }
}
