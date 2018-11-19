package com.rws.jsonclassifier;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
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
import java.util.Iterator;

public class Labeler extends AppCompatActivity {

    private File mother_path;
    private File original_path;
    private File destiny_path;

    private File currentFile;
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
        changeFile();

    }

    private void label(Boolean answer){
        if(currentFile == null) {
            Toast.makeText(getBaseContext(), "There's no files!", Toast.LENGTH_SHORT).show();
            return;
        }
        String json_string;
        JSONObject json_obj = null;
        try {
            FileInputStream is = new FileInputStream(currentFile);
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
                    File file = new File(destiny_path + "/" + currentFile.getName());
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(json_obj.toString());
                    output.close();
                    currentFile.delete();
                    //Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(getBaseContext(), "path isn't avilable", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getBaseContext(), "path doasn't exist", Toast.LENGTH_LONG).show();
        }
        //if(currentFile.delete()) Toast.makeText(getBaseContext(), "json labeled!", Toast.LENGTH_SHORT).show();
        changeFile();
    }

    private void changeFile(){
        if(list == null || (list.length-1<index)) {
            LinearLayout parent = findViewById(R.id.container);
            parent.removeAllViews();
            TextView n = new TextView(this);
            String val = "There's no JSONs to label! Put more .json files in the folder " + original_path.getAbsolutePath();
            n.setText(val);
            n.setBackground(getDrawable(R.drawable.layout_bg));
            parent.addView(n);
        }
        else {
            String json;
            //TextView textV = findViewById(R.id.jsonShower);
            try {
                FileInputStream is = new FileInputStream(list[index]);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                JSONObject obj = new JSONObject(json);
                LinearLayout parent = findViewById(R.id.container);
                parent.removeAllViews();
                printFields(obj, parent);
                /*LinearLayout parent = findViewById(R.id.container);
                parent.removeAllViews();
                Iterator<?> keys = obj.keys();
                while(keys.hasNext() ) {
                    LinearLayout LL = new LinearLayout(this);
                    LL.setBackground(getDrawable(R.drawable.layout_bg));
                    LL.setOrientation(LinearLayout.VERTICAL);

                    LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                    LL.setWeightSum(6f);
                    LL.setLayoutParams(LLParams);
                    parent.addView(LL);

                    TextView title = new TextView(this);
                    String key = (String)keys.next();
                    title.setText(key);
                    title.setTypeface(null, Typeface.BOLD);
                    //title.setBackground(getDrawable(R.color.transparent));
                    LL.addView(title);
                    String val = "";
                    if (obj.get(key) instanceof JSONObject ) {
                        JSONObject xx = new JSONObject(obj.get(key).toString());

                    } else {
                        val = obj.get(key).toString();
                    }

                    LinearLayout son = new LinearLayout(this);
                    son.setBackground(getDrawable(R.drawable.layout_bg));
                    son.setOrientation(LinearLayout.VERTICAL);

                    LayoutParams par = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                    LL.setWeightSum(6f);
                    LL.setLayoutParams(par);



                    TextView sonTXT = new TextView(this);
                    sonTXT.setText(val);
                    sonTXT.setBackground(getDrawable(R.color.transparent));
                    son.addView(sonTXT);
                    LL.addView(son);
                }
                //textV.setText(obj.toString().replaceAll(",", ",\n"));*/
            } catch (FileNotFoundException e) {
                //textV.setText(e.getMessage());
            } catch (UnsupportedEncodingException e) {
               // textV.setText(e.getMessage());
            } catch (IOException e) {
                //textV.setText(e.getMessage());
            } catch (JSONException e) {
                //textV.setText(e.getMessage());
            }
            currentFile = list[index];

            index++;
        }
    }
    private void printFields(JSONObject obj, LinearLayout parent){
        try{
            Iterator<?> keys = obj.keys();
            while(keys.hasNext() ) {
                LinearLayout LL = new LinearLayout(this);
                LL.setBackground(getDrawable(R.drawable.layout_bg));
                LL.setOrientation(LinearLayout.VERTICAL);

                LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                LL.setWeightSum(6f);
                LL.setLayoutParams(LLParams);
                parent.addView(LL);

                TextView title = new TextView(this);
                String key = (String)keys.next();
                title.setText(key);
                title.setTypeface(null, Typeface.BOLD);
                //title.setBackground(getDrawable(R.color.transparent));
                LL.addView(title);
                Object n = obj.get(key);
                if (obj.get(key) instanceof JSONObject ) {
                    JSONObject xx = new JSONObject((obj.get(key)).toString());
                    printFields(xx, LL);
                }
                else{
                    String val = obj.get(key).toString();
                    LinearLayout son = new LinearLayout(this);
                    son.setBackground(getDrawable(R.drawable.layout_bg));
                    son.setOrientation(LinearLayout.VERTICAL);

                    LayoutParams par = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                    LL.setWeightSum(6f);
                    LL.setLayoutParams(par);

                    TextView sonTXT = new TextView(this);
                    sonTXT.setText(val);
                    sonTXT.setBackground(getDrawable(R.color.transparent));
                    son.addView(sonTXT);

                    LL.addView(son);
                }
            }
            //textV.setText(obj.toString().replaceAll(",", ",\n"));
            //textV.setText(e.getMessage());
        } catch (JSONException e) {
            //textV.setText(e.getMessage());
        }
    }
}
