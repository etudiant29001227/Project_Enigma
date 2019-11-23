package com.example.project_e;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener {

    private Button start,option;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.start = findViewById(R.id.start);
        this.option = findViewById(R.id.option);

        gestureDetector = new GestureDetectorCompat(this,this);
        gestureDetector.setOnDoubleTapListener(this);

        start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), LevelActivity.class);
                startActivity(otherActivity);
                finish();

            }
        });
        option.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(otherActivity);
                finish();

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        System.out.println("Single tap");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        System.out.println("double tap 1");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        System.out.println("double tap 2");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
