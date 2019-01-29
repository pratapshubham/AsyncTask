package com.example.braintech.asynctaskdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btn_start;
    ImageView imgShowData;
    String image;
    String url1 = "https://api.androidhive.info/contacts/";
    ProgressDialog progressDialog;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getId();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });

    }

    public void getId() {
        btn_start = (Button) findViewById(R.id.btn_start);
        imgShowData = (ImageView) findViewById(R.id.imgShowData);
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("Data----->", "onPre");
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonStr = makeServiceCall(url1);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");


                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        Log.d("ID--->", id);
                        Log.d("Name--->", name);
                        Log.d("email--->", email);
                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");
                        Log.d("phone--->", mobile);

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("Data----->", "onPost");
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                Log.d("Data----->", "Try Block");

                Log.d("Data----->", "Attach to Image");
                // Picasso library to display the image from URL
                Picasso.get().load(url1).into(imgShowData);
                Log.d("Data----->", "Finish");

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


            /*Log.d("Data----->","doBackground");

            String current = null;
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(url1);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    *//*while (data != -1) {
                        current += (char) data;
                        data = isw.read();


                    }
                    System.out.print(current);*//*
                    if (data != -1)
                    {
                        try
                        {
                            JSONObject jsonObj = new JSONObject(String.valueOf(isw));

                            JSONArray contacts = jsonObj.getJSONArray("contacts");

                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                String id = c.getString("id");
                                String name = c.getString("name");
                                String email = c.getString("email");
                                String address = c.getString("address");
                                String gender = c.getString("gender");

                                // Phone node is JSON Object
                                JSONObject phone = c.getJSONObject("phone");
                                String mobile = phone.getString("mobile");
                                String home = phone.getString("home");
                                String office = phone.getString("office");

                                // tmp hash map for single contact
                                HashMap<String, String> contact = new HashMap<>();

                                // adding each child node to HashMap key => value
                                contact.put("id", id);
                                contact.put("name", name);
                                contact.put("email", email);
                                contact.put("mobile", mobile);

                                // adding contact to contact list
                                Log.d("Data", String.valueOf(contact));
                            }
                        }catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }
                    }else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                    // return the data to onPostExecute method
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            Log.d("Data----->","Last Current");*/

    }

    public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



}

   /* public class Async extends AsyncTask<ImageView, Void, Bitmap>
    {

        @Override
        protected void onPreExecute() {
            Log.d("Data---->","onPre");
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            Log.d("Data---->","doInBackground");

            imgShowData = imageViews[0];
            return download_Image((String)imgShowData.getTag());
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                Log.d("Data---->","download_Images");
                if (null != bmp)
                    return bmp;

            }catch(Exception e){
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            Log.d("Data---->","onPost");
            imgShowData.setImageBitmap(result);
        }
    }*/


