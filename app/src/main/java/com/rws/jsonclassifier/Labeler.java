package com.rws.jsonclassifier;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Labeler extends AppCompatActivity {

    private File mother_path;
    private File original_path;
    private File destiny_path;
    private File[] list;
    private int index;
    private final String fieldname = "training";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.labeler);
        mother_path = new File(Environment.getExternalStorageDirectory(), "JSON_Labeler");
        original_path = new File(mother_path, "originals");
        destiny_path = new File(mother_path, "labeled");

        Button yes = findViewById(R.id.button_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label(true);
            }
        });

        Button no = findViewById(R.id.button_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label(false);
            }
        });

        list = original_path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        index = 0;
        if(list != null && list.length > 0){
            changeFile();
        }

    }

    private void label(Boolean answer){
        String json_string;
        JSONObject json_obj = null;
        try {
            FileInputStream is = new FileInputStream(list[index]);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json_string = new String(buffer, "UTF-8");
            json_obj = new JSONObject(json_string);
            json_obj.put(fieldname,answer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(json_obj !=null && destiny_path.exists()){
            if(destiny_path.canWrite()){
                try {
                    Writer output = null;
                    File file = new File(destiny_path + "/" + list[index].getName());
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(json_obj.toString());
                    output.close();
                    Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(getBaseContext(), "path isn't avilable", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getBaseContext(), "path doasn't exist", Toast.LENGTH_LONG).show();
        }

        changeFile();
    }

    private void changeFile(){
        if(list == null || (list.length-1<index)) {

        }
        else {
            String json;
            TextView textV = findViewById(R.id.jsonShower);
            try {
                FileInputStream is = new FileInputStream(list[index]);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                JSONObject obj = new JSONObject(json);
                textV.setText(obj.toString().replaceAll(",", ",\n"));
            } catch (FileNotFoundException e) {
                textV.setText(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                textV.setText(e.getMessage());
            } catch (IOException e) {
                textV.setText(e.getMessage());
            } catch (JSONException e) {
                textV.setText(e.getMessage());
            }
            index++;
        }
    }
}
