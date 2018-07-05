package com.rws.jsonclassifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final Context e = this;
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    Intent next = new Intent(e, MainActivity.class);
                    startActivity(next);
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