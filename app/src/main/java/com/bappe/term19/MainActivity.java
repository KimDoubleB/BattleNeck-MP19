package com.bappe.term19;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    /*Wizets*/
    private Button signUp, signIn;
    private EditText idText, pwText;

    public String id, pw;

    // SharedPreference for user auto login
    public SharedPreferences sh_UserInfo;
    public SharedPreferences.Editor toEdit;
    public boolean isOK = false; // User is legitimate?


    public static HashMap<String, String> users = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);

        FirebaseApp.initializeApp(this);
        applySharedPreference();
        getUserData();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //데이터 전달하기
                id = idText.getText().toString();
                pw = pwText.getText().toString();

                if (users.containsKey(id)) {
                    if (users.get(id).equals(pw)) {
                        // If user is legitimate user, finish()
                        sharedPreference();
                        Intent intent2 = new Intent(getApplicationContext(), MainHomeActivity.class);
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "PW is incorrect !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID is incorrect !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 회원가입
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
                //             intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getUserData();
    }

    public void sharedPreference() {
        sh_UserInfo = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_UserInfo.edit();
        toEdit.putString("UserId", id);
        toEdit.putString("UserPw", pw);
        toEdit.commit();
    }

    public void applySharedPreference() {
        sh_UserInfo = getSharedPreferences("Login Credentials", MODE_PRIVATE);

        if (sh_UserInfo != null && sh_UserInfo.contains("UserId")) {
            String id2 = sh_UserInfo.getString("UserId", "Default");
            idText.setText(id2);
            String pw2 = sh_UserInfo.getString("UserPw", "Default");
            pwText.setText(pw2);
        }
    }

    public void getUserData() {
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("id_list/");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // All data in Firebase DB
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract ID and PW
                    String dataID = ((HashMap<String, Object>) snapshot.getValue()).get("id").toString();
                    String dataPW = ((HashMap<String, Object>) snapshot.getValue()).get("pw").toString();
                    Log.d("Message", dataID + " " + dataPW);

                    users.put(dataID, dataPW);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
