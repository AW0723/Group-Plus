package com.example.group_plus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;

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
    private HashMap<String, Integer> memberProg;
    //TODO add uuid for groups
    private int groupID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Initialize Firebase Auth and user
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();

        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //TODO add group uuid
        mProgressReference = mDatabase.child("Groups").child("1").child("Goals").child("Reading").child("Progress");

        // Add listener for database change
        ValueEventListener progressListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Progress is " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mProgressReference.addValueEventListener(progressListener);

        // Add onClickListener for save button
        Button saveBtn = this.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(view -> {
            EditText num1View = this.findViewById(R.id.books_read);
//            EditText num2View = this.findViewById(R.id.number2);

            int num1Text = Integer.parseInt(num1View.getText().toString());
            mDatabase.child("Users").child(currUser.getUid()).child(Integer.toString(groupID)).child("Reading").setValue(num1Text);
            Log.d(TAG, "Updated value in database: " + num1Text);
            updateProgress();
        });
    }

    private void updateProgress() {
        ArrayList<String> members = new ArrayList<>();

        mDatabase.child("Groups").child("1").child("Members").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                String membersString = String.valueOf(task.getResult().getValue());
                Log.d("firebase", membersString);

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(membersString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < jsonArray.length(); i++) {
                    try {
                        members.add(jsonArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        int sumProgress = 0;
//        for(String i : members) {
//            mDatabase.child("Users").child(i).child("1").child("Reading").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.e("firebase", "Error getting data", task.getException());
//                    }
//                    else {
//                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        sumProgress += task.getResult().getValue();
//                    }
//                }
//            });
//        }
    }

    public void onTestClicked(View view) {
        Map<String, Integer> userContribution = new HashMap<>();
        userContribution.put("OB6vPDVGsLg8PrjQxUAIpXEUobf1", 5);
        userContribution.put("0gfhcxZXR4bzk9Nc0t46LYw8lJV2", 3);
        JSONObject userContributionJSON = new JSONObject(userContribution);
        Log.d(TAG, userContributionJSON.toString());

        mDatabase.child("test").setValue(userContribution);

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