package com.example.a4nature.HomeScreen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a4nature.R;

public class Feedback extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        EditText suggestion = (EditText)findViewById(R.id.suggestion);
        Button button = (Button)findViewById(R.id.send_suggestion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = "andan223@gmail.com";
                String subject = "Feedback from 4nature App";
                String message = suggestion.getText().toString().trim();

                JavaMailAPI javaMailAPI = new JavaMailAPI(Feedback.this, mail, subject, message);
                javaMailAPI.execute();
            }
        });
    }
}