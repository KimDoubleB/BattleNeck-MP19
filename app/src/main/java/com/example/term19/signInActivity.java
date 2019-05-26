package com.example.term19;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class signInActivity extends Activity {

    EditText idText, pwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);

        //UI 객체생성
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);

    }

    public void OnSignUp(View v) {
        //데이터 전달하기
        Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
        //             intent.putExtra("data", "Test Popup");
        startActivity(intent);

        //액티비티(팝업) 닫기
        // finish();
    }
    //확인 버튼 클릭
    public void mOnClose(View v) {
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
                    Intent intent = new Intent();
                    intent.putExtra("result", true);
                    setResult(1, intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기

        Intent intent = new Intent();
        intent.putExtra("result", false);
        setResult(1, intent);
        finish();
    }

}
