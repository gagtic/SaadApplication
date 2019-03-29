package com.example.saadapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClickActivity extends AppCompatActivity {

    String address, id;
    Button time, date, appointment;
    int mYear, mMonth, mDay, mHour, mMinute;
    TextView address_view, date_view, time_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            address = bundle.getString("address");
            id = bundle.getString("id");
        }
        time = findViewById(R.id.select_time);
        date = findViewById(R.id.select_date);
        appointment = findViewById(R.id.appointment);

        address_view = findViewById(R.id.address);
        address_view.setText(address);
        date_view = findViewById(R.id.date);
        time_view = findViewById(R.id.time);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ClickActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String time_str = hourOfDay + " : " + minute;
                                time_view.setText(time_str);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ClickActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date_str = String.valueOf(dayOfMonth) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(year);
                                date_view.setText(date_str);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_view.getText().equals("Select Date") && time_view.getText().toString().equals("Select Time")){
                    Toast.makeText(ClickActivity.this, "Error: Please Select A Date And Time For Appointment", Toast.LENGTH_SHORT).show();

                }else{
                    new MakeAppointment().execute();
                }

            }
        });
    }

    ProgressDialog progressDialog;

    private class MakeAppointment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ClickActivity.this);
            progressDialog.setMessage("Making an Appointment");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://upride.000webhostapp.com/android/insert_booking.php";
            RequestQueue requestQueue = Volley.newRequestQueue(ClickActivity.this);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();
                            progressDialog.dismiss();
                            Toast.makeText(ClickActivity.this, "Appointment Made", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            progressDialog.dismiss();
                            Toast.makeText(ClickActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
//                    This is how you send parameters to the server

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("id", id);
                    param.put("date", date_view.getText().toString());
                    param.put("time", time_view.getText().toString());

                    return param;
//                    return new HashMap<String, String>();
                }
            };
            requestQueue.add(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

}
