package com.example.tofa7ashop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView username;
    TextView userphone;
    FirebaseAuth mAuth;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = openOrCreateDatabase("users", MODE_PRIVATE, null);

        Button logoutButton = findViewById(R.id.logout);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address
            String email = user.getEmail();
            String firebaseUserId = user.getUid();

            // Display email in the username TextView
            username = findViewById(R.id.userName);
            username.setText("Email: " + email);

            // Retrieve and display phone from SQLite database
            String phone = getPhoneFromDatabase(firebaseUserId);
            if (phone != null) {
                userphone = findViewById(R.id.userPhone);
                userphone.setText("Phone: " + phone);
            } else {
                Toast.makeText(this, "Phone not found in the database", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        // For instance, if you want to navigate back to the login screen
        Intent intent = new Intent(this, LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private String getPhoneFromDatabase(String firebaseUserId) {
        String phone = null;

        Cursor cursor = mDatabase.rawQuery("SELECT phone FROM users WHERE firebase_user_id = ?", new String[]{firebaseUserId});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("phone");

            // Check if the column index is valid (not -1)
            if (columnIndex != -1) {
                phone = cursor.getString(columnIndex);
            } else {
                // Handle the case where the column is not found
                Toast.makeText(this, "Phone column not found in the database", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
        return phone;
    }

}

