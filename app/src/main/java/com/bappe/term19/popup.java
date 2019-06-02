package com.bappe.term19;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class popup extends Activity {
    int count=0;
    Bundle rec;
    Intent dintent;
    AlertDialog alertDialog;
    public static boolean isActive = false;
    public static Activity _popup;
    protected void onCreate(Bundle savedInstanceState) {

        rec=new Bundle();
        //count=rec.getInt("count");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        _popup=popup.this;

        Log.d(this.getClass().getName(),"111112222222");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.DialogTheme);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if(alertDialog != null)
        {alertDialog.show();}
        alertDialog.setCancelable(false);

    }
    protected void onDestroy() {
        dintent = new Intent(this,MainActivity.class);
        super.onDestroy();

        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
            Log.d(this.getClass().getName(),"1111199999");
        }

        //String ccc=count+"";
        //Log.d(this.getClass().getName(),"11111ocount"+ccc);


//           if(count==1) {
//               dintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//               startActivity(dintent);
//           }//Log.d(this.getClass().getName(),"111110000000");
    }
}





