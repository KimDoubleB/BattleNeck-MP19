package com.bappe.term19;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import static com.bappe.term19.App.CHANNEL_ID;

// Press button from main to start foreground service.
// It implemented a immortal service.

public class MyService extends Service {
    Intent dialogintent;
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi;
    private SensorManager sm;
    private Sensor oriSensor;
    private SensorEventListener oriL;
    private float oy, ox, oz;

    // Vibration Variable
    public Vibrator vibrator;

    //public String id;
    public SharedPreferences sh_Score;
    public SharedPreferences.Editor toEdit;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!FirebasePost.isActive){
            this.onDestroy();
        }

        //  Get current ID
        // id = (String)intent.getExtras().get("ID");
        //  Log.d("MESSAGE", "rprt0  "+id);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // Create intents for alerts.
        Intent intent1 = new Intent(MyService.this, MainActivity.class);
        // If you press notification, Pendingintent will work.
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);


        // The current version check version can only be run on Oreo or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notifi = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Battle neck")
                    .setContentText("Right posture running")
                    .setSmallIcon(R.drawable.splash)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notifi_M.notify(1, Notifi);


        // Create objects with a handle at the same time as the thread.
        myServiceHandler handler = new myServiceHandler();

        // Call thread when service is started
        thread = new ServiceThread(handler);
        thread.start();

        // Connecting with intents for screen blocks
        dialogintent =new Intent(this,popup.class);
        // The place where the image activity is displayed.
        dialogintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Import Sensormanager instance
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Direction sensor
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        // Continue to check the value of the sensor changing through the SensorEventListener
        oriL = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {  // Called whenever direction sensor value changes.
                //ox.setText(Float.toString(event.values[0]));
                oy = event.values[1];  // Y direction
                Log.i("SENSOR", "VALUE." + oy);
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        // Delay settings for the sensor. This will result in a lower value to the riser
        sm.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Recreation and onStartCommand() Call (null intent)
        return START_STICKY;
    }


    // Operation of the destroy function at the end of the service.
    public void onDestroy() {
        Toast.makeText(MyService.this, "Service finishing", Toast.LENGTH_LONG).show();
        // Notifi_M.cancel(1);
        super.onDestroy();
        thread.stopForever();
        thread = null;
    }

    // Handler function to process values received from threads.
    class myServiceHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            int data;
            // If the angle is between 5 and 50 degrees, perform the corresponding function.
            if (oy < -5 && oy > -60) {
                // Run window for alerts.
                startActivity(dialogintent);

                // Having information for the ranking system.
                data = sharedPreference(-1);
                // Vibrating
                vibrator.vibrate(1000);

                Log.i("SENSOR", "Sensorvalue." + oy);
            }
            else{
                // +5 for correct positioning score
                data = sharedPreference(5);
                //FirebasePost.writeNewPost(id, FirebasePost.userScores.get(id)+5);
            }
            Log.d("Message", "value: " + data);

        }

        // Use sharedpreference for storing ranking data.
        public int sharedPreference(int num) {
            sh_Score = getSharedPreferences("Score", MODE_PRIVATE);
            int currentScore;

            if (sh_Score != null && sh_Score.contains("score")) {
                currentScore = sh_Score.getInt("score", 0) + num;
            }else{
                currentScore = num;
            }

            toEdit = sh_Score.edit();
            toEdit.putInt("score", currentScore);
            toEdit.commit();

            return currentScore;
        }

    }

}