package com.example.androidapp;

import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            ///////init
            Button btOk = findViewById(R.id.bt_ok);
            //ImageButton btHelp = findViewById(R.id.bt_help);

            ///////

            //////handlers
            btOk.setOnClickListener((v)->{
                //int timerMin = Integer.parseInt(((EditText)findViewById(R.id.num_sleepTimerMin)).getText().toString());
                PlayerSettings settings;
                try {
                    settings = new PlayerSettings(
                            Integer.parseInt(((EditText)findViewById(R.id.num_sleepIncrement)).getText().toString()),
                            Integer.parseInt(((EditText)findViewById(R.id.num_sleepCooldown)).getText().toString()));
                    //Toast.makeText(this,settings.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("cooldown", settings.cooldown); intent.putExtra("increment", settings.increment);
                    startActivity(intent);
                }
                catch (Exception ex){Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG);}


            });
            //////

        }
        catch (Exception ex){
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}