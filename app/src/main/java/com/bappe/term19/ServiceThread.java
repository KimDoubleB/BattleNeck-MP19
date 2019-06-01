package com.bappe.term19;

import android.os.Handler;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;

    public ServiceThread(MyService.myServiceHandler handler) {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        while (isRun) {
            handler.sendEmptyMessage(0);//
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }

        }

    }


}