package com.rws.jsonclassifier;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Labeler extends AppCompatActivity {

    private File mother_path;
    private File original_path;
    private File destiny_path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labeler);
        mother_path = new File(Environment.getExternalStorageDirectory(), "JSON_Labeler");
        original_path = new File(mother_path, "originals");
        destiny_path = new File(mother_path, "labeled");

        File [] files = original_path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        String json;
        if(files != null && files.length > 0){
            TextView textV = findViewById(R.id.jsonShower);
            try {
                FileInputStream is = new FileInputStream(files[0]);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                JSONObject obj = new JSONObject(json);
                textV.setText(obj.toString().replaceAll(",",",\n"));
            } catch (FileNotFoundException e) {
                textV.setText(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                textV.setText(e.getMessage());
            } catch (IOException e) {
                textV.setText(e.getMessage());
            } catch (JSONException e) {
                textV.setText(e.getMessage());
            }
        }

    }
}
