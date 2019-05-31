package com.bappe.term19;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUpActivity extends Activity {
    EditText idText, pwText, genderText, ageText;
    Button OkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        FirebasePost.readDataFirebase();


        //UI 객체생성
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        genderText = findViewById(R.id.gender);
        ageText = findViewById(R.id.age);
        OkBtn = findViewById(R.id.okButton);

        OkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // check the empty or incorrect data
                if (idText.getText().toString().isEmpty()
                        || pwText.getText().toString().isEmpty()
                        || ageText.getText().toString().isEmpty()
                        || genderText.getText().toString().isEmpty()
                ) {
                    Toast.makeText(getApplicationContext(), "Enter the correct information !", Toast.LENGTH_SHORT).show();
                }
                else if(!isInteger(ageText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Enter the correct information !", Toast.LENGTH_SHORT).show();
                }
                else {

                    String id = idText.getText().toString();
                    String pw = pwText.getText().toString();
                    int age = Integer.parseInt(ageText.getText().toString());
                    String gender = genderText.getText().toString();
                    int score = 0;

                    FirebasePost data = new FirebasePost(id, pw, age, gender,score);

                    // 중복제거하려했는데 안됨...
                    data.addDataFirebase();
                    finish();

                    //
//                    if(!data.addDataFirebase()){
//                        //액티비티(팝업) 닫기
//                        finish();
//                    }else{
//                        Toast.makeText(getApplicationContext(), "ID is duplicated !", Toast.LENGTH_SHORT).show();
//                    }

                }
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
        finish();
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
