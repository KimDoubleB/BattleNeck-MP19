package com.bappe.term19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    /*Wizets*/
    private Button signUp;
    private EditText idText, pwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signUp = findViewById(R.id.signUp);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);


        FirebaseApp.initializeApp(this);

        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
                //             intent.putExtra("data", "Test Popup");
                startActivity(intent);
            }
        });

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        boolean isLogined = data.getBooleanExtra("result", false);
//        if(isLogined){
//            Toast.makeText(getApplicationContext(), "Login Success !", Toast.LENGTH_SHORT).show();
//            Intent intent2 = new Intent(getApplicationContext(), MainHomeActivity.class);
//            startActivity(intent2);
//            finish();
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Login Failed !", Toast.LENGTH_SHORT).show();
//        }
//    }





    public void OnSignUp(View v) {
        //데이터 전달하기
        Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
        //             intent.putExtra("data", "Test Popup");
        startActivity(intent);

    }
    //확인 버튼 클릭
    public void Login(View v) {
        //데이터 전달하기
        final String id = idText.getText().toString();
        final String pw = pwText.getText().toString();

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("id_list/");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean isOK = false; // User is legitimate?

                // All data in Firebase DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Extract ID and PW
                    String dataID = ((HashMap<String, Object>)snapshot.getValue()).get("id").toString();
                    String dataPW = ((HashMap<String, Object>)snapshot.getValue()).get("pw").toString();
                    Log.d("Message", dataID + " "+ dataPW);

                    // If there is data which is entered by user, this user is legitimate user
                    if(dataID.equals(id) && dataPW.equals(pw)){
                        isOK = true;
                    }
                }

                // if user is undesired user,
                if(!isOK){
                    Toast.makeText(getApplicationContext(), "ID or PW is incorrect !", Toast.LENGTH_SHORT).show();
                }
                else{
                    // If user is legitimate user, finish()
                    Intent intent2 = new Intent(getApplicationContext(), MainHomeActivity.class);
                    startActivity(intent2);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
