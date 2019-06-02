package com.bappe.term19;

import android.os.Handler;
import android.util.Log;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun = true;

    public ServiceThread(MyService.myServiceHandler handler)
    {
        this.handler = handler;
    }

    public void stopForever() {
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run() {
        while (isRun) {
            handler.sendEmptyMessage(0);    // 핸들러 호출
            try {
                Thread.sleep(3000);
                popup p=(popup)popup._popup;
                if(p!=null)
                {
                    Log.d("Message", "POP UP FINISH");
                    p.finish();
                }
            } catch (Exception e) {
            }

        }

    }


}