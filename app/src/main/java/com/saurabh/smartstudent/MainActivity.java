package com.saurabh.smartstudent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.roll_no);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchData().execute(inputLayout.getEditText().getText().toString());
            }
        });
    }

    class FetchData extends AsyncTask<String, Void, Integer> {
        String URL = "http://192.168.1.100/Attendance/GetStudentData.php";
        ProgressDialog pd;
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Getting Data...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                URL+="?roll="+params[0];
                java.net.URL url = new URL(URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                Scanner sc = new Scanner(connection.getInputStream());
                String s = "";
                while (sc.hasNextLine()) {
                    s += sc.nextLine();
                }
                JSONObject object = new JSONObject(s);
                HashMap<String,Boolean> map = new HashMap<>(object.length());
                Iterator<String> iterator=object.keys();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    map.put(key,object.getBoolean(key));
                }
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                intent.putExtra("data",map);
                this.intent=intent;
                return 0;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return 1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            pd.dismiss();
            if(integer==0&&intent!=null){
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
