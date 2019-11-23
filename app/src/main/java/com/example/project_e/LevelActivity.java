package com.example.project_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelActivity extends AppCompatActivity {
    private Button enigma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        this.enigma = findViewById(R.id.difficulty5);

        enigma.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),MapsActivity.class);
                otherActivity.putExtra("enigma","enigme1");
                startActivity(otherActivity);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
