package com.rws.jsonclassifier;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class Initializer extends Thread {
    public static boolean ready = false;
    private AppCompatActivity context;
    public Initializer(AppCompatActivity c){
        context = c;
    }


    public void run(){
        {
            DataManager.status = DataManager.ON_EXECUTION;
            //Here the app folders will be checked and/or created.
            //When all is done, change "ready" to true so the timer-thread can continue

            DataManager.mother_path = new File(Environment.getExternalStorageDirectory(), "JSON_Labeler");
            DataManager.original_path = new File(DataManager.mother_path, "originals");
            DataManager.destiny_path = new File(DataManager.mother_path, "labeled");

            final int CUSTOM = 1;  // The request code
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CUSTOM);
                run();
            }

            boolean mother = checkMother();
            boolean original = checkOriginal();
            boolean destiny = checkDestiny();

            if(mother&&original&&destiny) {
                DataManager.status = DataManager.ALLSUCCESS;
            } else{
                DataManager.status = DataManager.FATALERROR;
                //FATAL EXCEPTION. FILE SYSTEM UNREACHABLE.
            }

            ready = true;
        }
    }

    private boolean checkMother() {
        if(DataManager.mother_path.exists()&&DataManager.mother_path.canWrite()) return true; //The path exists and the app can write.

        if(!DataManager.mother_path.exists()){
            //There's no mother path. Create
        }
        if(!DataManager.mother_path.canWrite()){
            //The mother path exists but the app has no permission to write on it.
        }

        return false;
    }

    private boolean checkDestiny() {
        return false;
    }

    private boolean checkOriginal() {
        return false;
    }

}
