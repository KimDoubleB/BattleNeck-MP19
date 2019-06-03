package com.bappe.term19;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        FirebasePost.getUserData();
        hd.postDelayed(new splashhandler(), 1600); // run hd handler in 1.6 second

    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class)); // After loading, move to ChoiceFunction
            Splash.this.finish(); // Remove from loading page Activity stack
        }
    }

    @Override
    public void onBackPressed() {
        // Don't allows the user to press the back button when moving from the initial flash screen
    }

}
