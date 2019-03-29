package com.example.saadapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Doctor> doctor_list = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.doctor_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new PopulateList().execute();
    }

    ProgressDialog progressDialog;

    private class PopulateList extends AsyncTask<Void, Void, Void> {
        private String status = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Getting Doctors");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://upride.000webhostapp.com/android/get_docters.php";
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json_response = new JSONObject(response);
                                JSONArray output = json_response.getJSONArray("response");
                                for(int i = 0; i < output.length(); i++){
                                    JSONObject doctor = output.getJSONObject(i);
                                    doctor_list.add(new Doctor(
                                            doctor.getString("name"),
                                            doctor.getString("address"),
                                            doctor.getString("speciallity"),
                                            doctor.getString("imagePath"),
                                            doctor.getString("id"),
                                            doctor.getString("type"),
                                            doctor.getString("date")
                                    ));
                                    ListAdapter listAdapter = new ListAdapter(doctor_list, getWindow().getDecorView());
                                    recyclerView.setAdapter(listAdapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.hide();
                            progressDialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            progressDialog.dismiss();
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
//                    This is how you send parameters to the server

//                    Map<String, String> param = new HashMap<String, String>();
//                    param.put("id", "value");
//                    return param;
                    return new HashMap<String, String>();
                }
            };
            requestQueue.add(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
        }
    }

}
