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

    AlertDialog alertDialog;//Define alertdialog
    public static Activity _popup;//Define activity name used by another class

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//set title with no title

        _popup=popup.this;//naming this class with _popup

        //apply theme specified at style.xml and create dialog as that
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.DialogTheme);
        alertDialog = alertDialogBuilder.create();

        //set dialog with not black screen behind
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //if there is no dialog, show this dialog
        if(alertDialog != null)
        {
            alertDialog.show();
        }
        //set can't react to touch when touched out of dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);

    }
    protected void onDestroy() {
        super.onDestroy();
        //delete exising dialog for preventing duplication
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}


