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

        // widget
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        genderText = findViewById(R.id.gender);
        ageText = findViewById(R.id.age);
        OkBtn = findViewById(R.id.okButton);

        // Sign up button
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

                    // protect duplicated data
                    if(data.addDataFirebase()){
                        // if it is OK? , finish
                            finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "ID is duplicated !", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Do not close when clicking outer layer
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // check integer
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
