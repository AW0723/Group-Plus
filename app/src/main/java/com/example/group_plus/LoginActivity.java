package com.example.group_plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "Login Activity";
    private TextView userEmail;
    private TextView userPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set login info from view
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void logInClicked(View view) {
        // Get user info from textviews
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();


        if(email.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "Enter email or password",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Attempt to login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, GroupActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}