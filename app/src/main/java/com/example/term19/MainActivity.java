package com.example.term19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    /*Wizets*/
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = findViewById(R.id.btnLogin);


        FirebaseApp.initializeApp(this);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), signInActivity.class);
                //             intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isLogined = data.getBooleanExtra("result", false);
        if(isLogined){
            Toast.makeText(getApplicationContext(), "Login Success !", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), MainHomeActivity.class);
            startActivity(intent2);
        }
        else{
            Toast.makeText(getApplicationContext(), "Login Failed !", Toast.LENGTH_SHORT).show();
        }
    }

}
