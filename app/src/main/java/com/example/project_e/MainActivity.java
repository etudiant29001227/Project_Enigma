package com.example.project_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start,option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.start = findViewById(R.id.start);
        this.option = findViewById(R.id.option);

        start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),Level.class);
                startActivity(otherActivity);
                finish();

            }
        });
        option.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),Option.class);
                startActivity(otherActivity);
                finish();

            }
        });


    }
}
