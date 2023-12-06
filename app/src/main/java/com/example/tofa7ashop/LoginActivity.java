package com.example.tofa7ashop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editTextemail, editTextPassword;
    Button loginbtn;
    TextView changepassword, registernow;
    FirebaseAuth mAuth;
    CheckBox rememberMeCheckBox;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextemail = findViewById(R.id.logEmail);
        editTextPassword = findViewById(R.id.logPassword);
        loginbtn = findViewById(R.id.loginButton);
        changepassword = findViewById(R.id.forgotpassword);
        registernow = findViewById(R.id.registerTextView);
        mAuth = FirebaseAuth.getInstance();
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        // Check if "Remember Me" was previously checked
        checkRememberMeState();

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                password = String.valueOf(editTextPassword.getText());
                email = String.valueOf(editTextemail.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter Email please", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password please", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Save "Remember Me" state if checkbox is checked
                                    if (rememberMeCheckBox.isChecked()) {
                                        saveRememberMeState(true);
                                    }

                                    Toast.makeText(LoginActivity.this, "LOGIN successful.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Check if "Remember Me" was previously checked
    private void checkRememberMeState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("remember", false);

        if (rememberMe) {
            rememberMeCheckBox.setChecked(true);
        }
    }

    // Save "Remember Me" state
    private void saveRememberMeState(boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remember", isChecked);
        editor.apply();
    }
}
