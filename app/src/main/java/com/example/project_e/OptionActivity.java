package com.example.project_e;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class OptionActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_fr:
                if (checked) {
                    LocaleHelper.setLocale(OptionActivity.this, "fr");
                    recreate();
                }
                    break;
            case R.id.radio_jp:
                if (checked) {
                    LocaleHelper.setLocale(OptionActivity.this, "jp");
                    recreate();
                }
                    break;
        }
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
