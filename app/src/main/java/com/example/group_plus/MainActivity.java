package com.example.group_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Main activity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        ArrayList<String> members = new ArrayList<>();
        members.add("OB6vPDVGsLg8PrjQxUAIpXEUobf1");
        members.add("0gfhcxZXR4bzk9Nc0t46LYw8lJV2");
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent;

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            Log.d(TAG, "User logged in");

            //go to group activity if user is logged in
            intent = new Intent(this, GroupActivity.class);
        } else {
            Log.d(TAG, "User not logged in");

            //go to login activity if user if not logged in
            intent = new Intent(this, LoginActivity.class);
        }
        this.startActivity(intent);
    }
}