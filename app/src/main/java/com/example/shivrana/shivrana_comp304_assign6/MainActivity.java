package com.example.shivrana.shivrana_comp304_assign6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button smsButton;
    Button tripButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        declaration();
        //Calling the event method
        btnEvents();
    }

    //Declaration of all the things
    public void declaration(){
     smsButton = findViewById(R.id.smsButton);
     tripButton = findViewById(R.id.tripPlanBtn);
    }

    //All the btn Events are below
    public void btnEvents(){
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),smsActivity.class));
            }
        });

        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),WebActivity.class));
            }
        });
    }
}
