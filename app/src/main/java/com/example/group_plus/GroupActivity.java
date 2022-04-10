package com.example.group_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Button equalsBtn = this.findViewById(R.id.save_button);
        equalsBtn.setOnClickListener(view -> {
            EditText num1View = this.findViewById(R.id.editTextNumber);
            EditText num2View = this.findViewById(R.id.number2);

            String num1Text = num1View.getText().toString();
            String num2Text = num2View.getText().toString();

            int num1 = Utils.toIntNullsafe(num1Text);
            int num2 = Utils.toIntNullsafe(num2Text);

            int answer = num1 + num2;

            TextView resultView = this.findViewById(R.id.result);
            resultView.setText(String.valueOf(answer));
        });
    }


}