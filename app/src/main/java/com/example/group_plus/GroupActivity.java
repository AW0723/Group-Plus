package com.example.group_plus;

import androidx.appcompat.app.AppCompatActivity;

import org.json.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private final String TAG = "GroupActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currUser;
    private DatabaseReference mProgressReference;
    private int goal = 12;
    private int userProg = 0;
    //TODO add uuid for groups
    private int groupID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Log.d(TAG, "group");

        // Initialize Firebase Auth and user
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();

        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgressReference = mDatabase.child("progress");

        // Add listener for database change
        ValueEventListener progressListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Progress is " + dataSnapshot.getValue());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Data: " + jsonObject);
                Iterator<String> uidItr = jsonObject.keys();
                int sumProgress = 0;
                while(uidItr.hasNext()) {
                    String uid = uidItr.next();
                    try {
                        sumProgress += jsonObject.getInt(uid);
                        if(uid.equals(currUser.getUid())) {
                            userProg = jsonObject.getInt(uid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ProgressBar bookProgress = findViewById(R.id.book_progress_bar);
                int progress = (int) ((float) sumProgress / (float) goal * 100);
                bookProgress.setProgress(progress, true);

                TextView progressText = findViewById(R.id.book_progress_text);
                progressText.setText(Integer.min(sumProgress, goal) + "/" + goal + " Books");
                Log.d(TAG, progressText.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mProgressReference.addValueEventListener(progressListener);

        // Add onClickListener for save button
        Button addBtn = this.findViewById(R.id.add_button);
        addBtn.setOnClickListener(view -> {
            EditText num1View = this.findViewById(R.id.books_read);

            int num1Text = Integer.parseInt(num1View.getText().toString());
            mDatabase.child("progress").child(currUser.getUid()).setValue(userProg + num1Text);
            Log.d(TAG, "Updated value in database: " + num1Text);
        });
    }

    public void onTestClicked(View view) {
        Map<String, Integer> userContribution = new HashMap<>();
        userContribution.put("OB6vPDVGsLg8PrjQxUAIpXEUobf1", 5);
        userContribution.put("0gfhcxZXR4bzk9Nc0t46LYw8lJV2", 3);
        JSONObject userContributionJSON = new JSONObject(userContribution);
        Log.d(TAG, userContributionJSON.toString());

        mDatabase.child("progress").setValue(userContribution);

        Map<String, Integer> convertedUserContribution = new HashMap<>();
        JSONObject userContributionJSONObj = null;
        try {
            userContributionJSONObj = new JSONObject(userContributionJSON.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        convertedUserContribution = new HashMap<>();
        Iterator<String> uidItr = userContributionJSONObj.keys();
        while(uidItr.hasNext()) {
            String uid = uidItr.next();
            try {
                convertedUserContribution.put(uid, userContributionJSONObj.getInt(uid));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "hi" + convertedUserContribution);
    }
}