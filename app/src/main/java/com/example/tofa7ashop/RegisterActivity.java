// RegisterActivity.java
package com.example.tofa7ashop;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextemail, editTextPassword, editTextphone, editTextname;
    Button regBtn;
    TextView textView;
    FirebaseAuth mAuth;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextemail = findViewById(R.id.regEmail);
        editTextPassword = findViewById(R.id.regPassword);
        editTextname = findViewById(R.id.regName);
        editTextphone = findViewById(R.id.regPhone);
        regBtn = findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.textViewLogin);

        // Initialize SQLite database
        mDatabase = openOrCreateDatabase("users", MODE_PRIVATE, null);
        createTable(); // Method to create a table if it doesn't exist

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password, phone;
                name = String.valueOf(editTextname.getText());
                email = String.valueOf(editTextemail.getText());
                password = String.valueOf(editTextPassword.getText());
                phone = String.valueOf(editTextphone.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    // Save user details to SQLite database
                                    saveUserDetailsToDatabase(name, email, password, phone, firebaseUser.getUid());

                                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Method to create a table if it doesn't exist
    private void createTable() {
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, phone TEXT, firebase_user_id TEXT)");
    }

    // Method to save user details to SQLite database
    private void saveUserDetailsToDatabase(String name, String email, String password, String phone, String firebaseUserId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("firebase_user_id", firebaseUserId);

        mDatabase.insert("users", null, contentValues);
    }
}
