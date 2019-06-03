package com.bappe.term19;

import android.os.Handler;
import android.util.Log;

// Run the ServiceThread when it starts from the MyService.java File.

public class ServiceThread extends Thread {
    Handler handler;
    static boolean isRun = true;

    public ServiceThread(MyService.myServiceHandler handler)
    {
        this.handler = handler;
    }

    // Thread start
    public static void startForever() {
        isRun = true;
    }

    // Thread stop
    public static void stopForever() {
        isRun = false;
    }

    // Start the thread.
    public void run() {
        // Send information to the handler every 3 seconds if it is working.
        while (isRun) {
            // Send message to handler
            handler.sendEmptyMessage(0);
            try {
                // Thread sleep 3sec
                Thread.sleep(3000);
                popup p=(popup)popup._popup;
                if(p!=null)
                {
                    Log.d("Message", "POP UP FINISH");
                    p.finish();
                }
                Thread.sleep(3000);
            } catch (Exception e) {
            }

        }

    }


}