package com.bappe.term19;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainHomeActivity extends AppCompatActivity {
    private TextView tv_roll;

    public ListView listview;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        // Tab 2
        listview = findViewById(R.id.listview);
        setRanking();

        // Tab 1
        tv_roll = findViewById(R.id.tv_roll);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        userSensorListner = new UserSensorListener();
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("Data") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("Rank") ;
        tabHost1.addTab(ts2) ;

        // event listener
        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 실행 중이지 않을 때 -> 실행 */
                if (!running) {
                    running = true;
                    mSensorManager.registerListener(userSensorListner, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
                    mSensorManager.registerListener(userSensorListner, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                }

                /* 실행 중일 때 -> 중지 */
                else if (running) {
                    running = false;
                    mSensorManager.unregisterListener(userSensorListner);

                }
            }
        });

    }

    public void setRanking(){

        FirebasePost.getUserData();

        Iterator it = sortByValue(FirebasePost.userScores).iterator(); // sort by value

        // ArrayAdapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1){
            // set text color of listview
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.parseColor("#00D85A"));
                return view;
            }
        };

        while(it.hasNext()) {
            String dataID  = (String) it.next();
            Log.d("Message2", "RANK " + dataID + " = " + FirebasePost.userScores.get(dataID));
            String rankData = dataID + " " + FirebasePost.userScores.get(dataID);
            adapter.add(rankData);
        }

        listview.setAdapter(adapter);
    }
//
//    // Tab view 2 function
//    public void setRanking() {
//        id_score = new HashMap<>();
//
//        // Get a reference to our posts
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("id_list/");
//
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // All data in Firebase DB
//
//                String dataID, dataScore;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
//                    /*
//                    //    this.id = id;
//                        this.pw = pw;
//                        this.age = age;
//                        this.gender = gender;
//                    //    this.score = score;
//                    */
//
//                    dataID = ((HashMap<String, Object>) snapshot.getValue()).get("id").toString();
//                    dataScore = ((HashMap<String, Object>) snapshot.getValue()).get("score").toString();
//
//                    id_score.put(dataID, Integer.parseInt(dataScore));
//                }
//                Log.d("Message2", id_score.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//    }



    // Tab view 1 function
    /**
     * 1차 상보필터 적용 메서드
     */
    private void complementaty(double new_ts) {

        /* 자이로랑 가속 해제 */
        gyroRunning = false;
        accRunning = false;

        /*센서 값 첫 출력시 dt(=timestamp - event.timestamp)에 오차가 생기므로 처음엔 break */
        if (timestamp == 0) {
            timestamp = new_ts;
            return;
        }
        dt = (new_ts - timestamp) * NS2S; // ns->s 변환
        timestamp = new_ts;

        /* degree measure for accelerometer */
        mAccPitch = -Math.atan2(mAccValues[0], mAccValues[2]) * 180.0 / Math.PI; // Y 축 기준
        mAccRoll = Math.atan2(mAccValues[1], mAccValues[2]) * 180.0 / Math.PI; // X 축 기준

        /**
         * 1st complementary filter.
         *  mGyroValuess : 각속도 성분.
         *  mAccPitch : 가속도계를 통해 얻어낸 회전각.
         */
        temp = (1 / a) * (mAccPitch - pitch) + mGyroValues[1];
        pitch = pitch + (temp * dt);

        temp = (1 / a) * (mAccRoll - roll) + mGyroValues[0];
        roll = roll + (temp * dt);

        tv_roll.setText("roll : " + roll);

    }

    public class UserSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {

                /** GYROSCOPE */
                case Sensor.TYPE_GYROSCOPE:

                    /*센서 값을 mGyroValues에 저장*/
                    mGyroValues = event.values;

                    if (!gyroRunning)
                        gyroRunning = true;

                    break;

                /** ACCELEROMETER */
                case Sensor.TYPE_ACCELEROMETER:

                    /*센서 값을 mAccValues에 저장*/
                    mAccValues = event.values;

                    if (!accRunning)
                        accRunning = true;

                    break;

            }

            /**두 센서 새로운 값을 받으면 상보필터 적용*/
            if (gyroRunning && accRunning) {
                complementaty(event.timestamp);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


    public void onBackPressed() {
        finish();
    }

    // sort function by value
    public static List sortByValue(final Map map) {

        List<String> list = new ArrayList();

        list.addAll(map.keySet());
        Collections.sort(list,new Comparator() {
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });
//        Collections.reverse(list); // 주석시 오름차순
        return list;
    }

}
