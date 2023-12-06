package com.example.tofa7ashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextemail , editTextPassword  , editTextphone , editTextname , editTextsec ;
    Button regBtn ;
    TextView textView ;
    FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextemail =  findViewById(R.id.regEmail) ;
        editTextPassword = findViewById(R.id.regPassword) ;
        editTextname = findViewById(R.id.regName) ;
        editTextphone = findViewById(R.id.regPhone) ;
        regBtn = findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();
        editTextsec = findViewById(R.id.regSec);
        textView = findViewById(R.id.textViewLogin);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , LoginActivity.class) ;
                startActivity(intent);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  , email , password , phone , sec ;
                name  = String.valueOf(editTextname.getText());
                email  = String.valueOf(editTextemail.getText());
                password  = String.valueOf(editTextPassword.getText());
                phone  = String.valueOf(editTextphone.getText());
                sec  = String.valueOf(editTextsec.getText());

                if(TextUtils.isEmpty(email) ){
                    Toast.makeText(RegisterActivity.this , "Enter Email please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }
                if(TextUtils.isEmpty(password) ){
                    Toast.makeText(RegisterActivity.this , "Enter password please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }
                if(TextUtils.isEmpty(phone) ){
                    Toast.makeText(RegisterActivity.this , "Enter password please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }
                if(TextUtils.isEmpty(name) ){
                    Toast.makeText(RegisterActivity.this , "Enter name please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }
                if(TextUtils.isEmpty(sec)  ){
                    Toast.makeText(RegisterActivity.this , "Enter security question please" ,Toast.LENGTH_SHORT).show() ;
                    return ;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    Toast.makeText(RegisterActivity.this, "account created.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}