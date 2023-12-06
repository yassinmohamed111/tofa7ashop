package com.example.tofa7ashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button back , submit ;
    EditText editTextmail ;
    FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mAuth = FirebaseAuth.getInstance();
        editTextmail = findViewById(R.id.forgetEmail);
        back = findViewById(R.id.forgetBack);
        submit = findViewById(R.id.forgetBtn);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email , sec ;
                email  = String.valueOf(editTextmail.getText());


                if(TextUtils.isEmpty(email) ){
                    Toast.makeText(ForgetPasswordActivity.this , "Enter Email please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }


                String mail ;
                mail  = editTextmail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(mail)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPasswordActivity.this , "Email sent" ,Toast.LENGTH_SHORT).show() ;
                                    Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }


                            }
                        });



            }
        });

    }
}