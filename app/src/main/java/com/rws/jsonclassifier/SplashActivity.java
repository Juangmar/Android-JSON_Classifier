package com.rws.jsonclassifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    public static boolean ready;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final Context e = this;
        ready = false;
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(2000);
                    ready = true;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)

                }
            }
        };
        timer.start();
        while(!ready){
            //Wait until the loader's thread finishes.
        }
        Intent next = new Intent(e, MainActivity.class);
        startActivity(next);

    }
    //Destroy Welcome_screen.java after it goes to next activity
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}