package com.bappe.term19;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import static com.bappe.term19.App.CHANNEL_ID;

public class MyService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi;
    private SensorManager sm;
    private Sensor oriSensor;
    private SensorEventListener oriL;
    private float oy, ox, oz;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(MyService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notifi = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Battle neck")
                    .setContentText("Right posture running")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notifi_M.notify(1, Notifi);

        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);    // SensorManager 인스턴스를 가져옴
        oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);    // 방향 센서
        oriL = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {  // 방향 센서 값이 바뀔때마다 호출됨
                //ox.setText(Float.toString(event.values[0]));
                oy = event.values[1];  //y 값 출력
                Log.i("SENSOR", "VALUE." + oy);

                //oz.setText(Float.toString(event.values[2]));
                    /*if (oy < 0 && oy > -40) {
                        Toast.makeText(MyService.this, "고개 들어라 거북이 된다~", Toast.LENGTH_SHORT).show();
                        Log.i("SENSOR", "Sensorvalue."+oy);
                    }*/
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sm.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        Toast.makeText(MyService.this, "Service finishing", Toast.LENGTH_LONG).show();
        Notifi_M.cancel(1);
        super.onDestroy();
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            if (oy < 0 && oy > -50) {
                Toast.makeText(MyService.this, "고개 들어라 거북이 된다~", Toast.LENGTH_SHORT).show();
                Log.i("SENSOR", "Sensorvalue." + oy);
            }
        }

        ;
    }
}