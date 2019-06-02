package com.bappe.term19;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class popup extends Activity {
    Bundle rec;
    Intent dintent;
    AlertDialog alertDialog;
    public static Activity _popup;

    protected void onCreate(Bundle savedInstanceState) {

        rec=new Bundle();

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        _popup=popup.this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.DialogTheme);
        alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if(alertDialog != null)
        {
            alertDialog.show();
        }
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);

    }
    protected void onDestroy() {
        //dintent = new Intent(this,MainActivity.class);
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}




