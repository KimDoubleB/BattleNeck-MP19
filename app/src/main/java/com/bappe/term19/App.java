package com.bappe.term19;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class App extends Application {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    //when app is created do create notification before creating activity,service else.
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }
    //create notificatoin
    private void createNotificationChannel() {
        //if version of device is over Oreo, create notification channel with channel_id, name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Define object to use notification and enroll notification to notification manager
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}