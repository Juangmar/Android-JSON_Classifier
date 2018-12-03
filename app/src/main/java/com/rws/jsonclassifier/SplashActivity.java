package com.rws.jsonclassifier;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    public static int load_code;
    private AppCompatActivity t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        t = this;
        final Context e = this;
        load_code = DataManager.INITIALIZING;
        Thread timer= new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(3000);

                    Initializer load = new Initializer(t);
                    load.start();

                    while(!Initializer.ready){
                        //Wait until the loader's thread finishes.
                    }
                    Intent next = new Intent(e, MainActivity.class);
                    startActivity(next);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        timer.start();



    }
    //Destroy Welcome_screen.java after it goes to next activity
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}