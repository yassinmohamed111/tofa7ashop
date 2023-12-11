// RegisterActivity.java
package com.example.tofa7ashop;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextemail, editTextPassword, editTextphone, editTextname , editTextBirthdate;
    Button regBtn;
    TextView textView;
    FirebaseAuth mAuth;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnPickBirthdate = findViewById(R.id.btnPickBirthdate);
        btnPickBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



        editTextemail = findViewById(R.id.regEmail);
        editTextPassword = findViewById(R.id.regPassword);
        editTextname = findViewById(R.id.regName);
        editTextphone = findViewById(R.id.regPhone);
        editTextBirthdate = findViewById(R.id.regBirthdate);
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
                String name, email, password, phone , birthdate;
                name = String.valueOf(editTextname.getText());
                email = String.valueOf(editTextemail.getText());
                password = String.valueOf(editTextPassword.getText());
                phone = String.valueOf(editTextphone.getText());
                birthdate = String.valueOf(editTextBirthdate.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) ||TextUtils.isEmpty(birthdate) ) {
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
                                    saveUserDetailsToDatabase(name, email, password, phone, birthdate ,firebaseUser.getUid());

                                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        EditText editTextBirthdate = findViewById(R.id.regBirthdate);
                        editTextBirthdate.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Method to create a table if it doesn't exist
    private void createTable() {
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, password TEXT, phone TEXT, birthdate TEXT , firebase_user_id TEXT)");
    }

    // Method to save user details to SQLite database
    private void saveUserDetailsToDatabase(String name, String email, String password, String phone, String birthdate,String firebaseUserId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("birthdate" , birthdate);
        contentValues.put("firebase_user_id", firebaseUserId);

        mDatabase.insert("users", null, contentValues);
    }
}
