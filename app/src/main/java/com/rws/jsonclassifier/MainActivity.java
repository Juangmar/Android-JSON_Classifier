package com.rws.jsonclassifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDirectory();
        createDirectoryDest();
        refreshData();

        Button r = findViewById(R.id.refresh);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        Button go = findViewById(R.id.button_go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runLabeler();
            }

        });



    }
    public void runLabeler(){
        Intent next = new Intent(this, Labeler.class);
        startActivity(next);
    }
    public void refreshData(){
        int ready = checkReadyFiles();
        int done = checkDoneFiles();

        TextView e = findViewById(R.id.textView);
        e.setText("Right now there's:\n" + ready + " json files ready\n" + done + " json files labeled");

        TextView down = findViewById(R.id.message);
        down.setText("please, put the .json files you want to label in the following folder:\n\n" + DataManager.original_path.getAbsolutePath());
    }
    public int checkReadyFiles(){

        File [] files = DataManager.original_path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        if(files == null) return 0;
        return files.length;
    }
    public int checkDoneFiles(){
        File [] files = DataManager.destiny_path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        if(files == null) return 0;
        return files.length;
    }
    public void createDirectory() {
        if (!DataManager.mother_path.exists()) {
            final int CUSTOM = 1;  // The request code

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CUSTOM);
                createDirectory();
            } else {
                if (!DataManager.mother_path.mkdirs()) {
                    Log.d("App", "failed to create directory");
                } else {
                    //Fist Directory created!!!!
                    if (!DataManager.original_path.mkdirs()) {
                        Toast toast = Toast.makeText(this, "The child directories can't be created", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        notifyDirectory(DataManager.original_path);
                    }
                }
            }
        } else{
            //THE FIRST DIRECTORY ALREADY EXISTS
            if (!DataManager.original_path.exists()) {
                if (!DataManager.original_path.mkdirs()) {
                    Toast toast = Toast.makeText(this, "The child directories can't be created", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    notifyDirectory(DataManager.original_path);
                }
            }else{
                //ALL DIRECTORIES ARE CREATED
            }
        }

    }
    public void createDirectoryDest() {
        if (!DataManager.mother_path.exists()) {
            final int CUSTOM = 1;  // The request code

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CUSTOM);
                createDirectory();
            } else {
                if (!DataManager.mother_path.mkdirs()) Log.d("App", "failed to create directory");
            }
        }
        //THE FIRST DIRECTORY ALREADY EXISTS
        if (!DataManager.destiny_path.exists()) {
            if (!DataManager.destiny_path.mkdirs()) {
                Toast toast = Toast.makeText(this, "The child directory destiny can't be created", Toast.LENGTH_LONG);
                toast.show();
            } else {
                notifyDirectory(DataManager.destiny_path);
            }
        }else{
            //ALL DIRECTORIES ARE CREATED
        }
    }
    public void notifyDirectory(File e){
        Toast toast = Toast.makeText(this, "The directory has been created at "  + e.getAbsolutePath() +
                ".\n Now you can place your json files there to start labeling!", Toast.LENGTH_LONG);
        toast.show();
    }
}
