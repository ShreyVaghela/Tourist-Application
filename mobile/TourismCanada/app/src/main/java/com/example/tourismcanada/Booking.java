package com.example.tourismcanada;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Booking extends AppCompatActivity {
    Button getBus;
    EditText noOfPassengers;
    EditText date;
    int passengers;
    String sourceName;
    int sourceID=7,destID=4;
    String baseURL="https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev",user_id;

    TextView buses;
    private RequestQueue queue;
    ArrayAdapter<String> dataAdapter,listadapter;
    Integer sourceid[],dest[],nopass[],ids[],bus_id[],priceeach[];
    String sourcename[],destname[],busno[],arrtime[],deptime[];
    ListView getBusList;
    List<String> cities=new ArrayList<String>();
    List<String> buslist=new ArrayList<String>();
    int day,month,year;
    Calendar currentDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
        Intent intent=getIntent();
        destID=intent.getIntExtra("destId",2);
        user_id=intent.getStringExtra("user_id");
        queue= Volley.newRequestQueue(this);
        String URL=baseURL+"/getSources/"+destID;
        final Spinner source = (Spinner) findViewById(R.id.sourceID);
        currentDate=Calendar.getInstance();
        day=currentDate.get(Calendar.DAY_OF_MONTH);
        month=currentDate.get(Calendar.MONTH);
        year=currentDate.get(Calendar.YEAR);
        dataAdapter = new ArrayAdapter<String>(Booking.this, android.R.layout.simple_spinner_item, cities);
        final JsonObjectRequest jsonObject1=new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("outside response",response.toString());
                            if(response.has("result")){
                            Log.d("Response:",response.toString());
                            JSONArray jsonArray=response.getJSONArray("result");
                            ids=new Integer[jsonArray.length()];
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                ids[i]=jsonObject.getInt("sourceId");
                                cities.add(jsonObject.getString("name"));
                            }
                            dataAdapter.notifyDataSetChanged();
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            source.setAdapter(dataAdapter);
                            source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    sourceName=parent.getItemAtPosition(position).toString();
                                    sourceID=ids[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    sourceID=ids[0];
                                }
                            });
                        }else{
                                System.out.println("Did not receive the result for the backend for /getSources");
                            }} catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObject1);

        noOfPassengers=(EditText) findViewById(R.id.noOfPassengers);
        noOfPassengers.setText("1");
        date=(EditText) findViewById(R.id.dateID);
        getBus=(Button) findViewById(R.id.getBusID);
        buses=(TextView) findViewById(R.id.busesID);
        getBusList=(ListView)findViewById(R.id.listID);
        getBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buslist.clear();
                getBusList.setAdapter(null);
                buses.setText("");
                buses.setGravity(Gravity.LEFT);
                if(TextUtils.isEmpty(noOfPassengers.getText())){
                    Toast.makeText(Booking.this,"No. of passengers is required",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(date.getText().toString())){
                    Toast.makeText(Booking.this,"Date is required",Toast.LENGTH_LONG).show();
                }else {
                passengers=Integer.parseInt(noOfPassengers.getText().toString());
                //gotoPayment();
//                jsonParse();
                listadapter=new ArrayAdapter<String>(Booking.this, android.R.layout.simple_list_item_1,buslist);
                String URL=baseURL+"/getBuses/"+sourceID+"/"+destID;
                final JsonObjectRequest jsonObject=new JsonObjectRequest(Request.Method.GET, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.has("result")) {
                                        JSONArray jsonArray = response.getJSONArray("result");
                                        if (jsonArray.length() > 0) {
                                            buses.setText("\t\tBusNo\t\tArrival\t\t\tDeparture\t\t\tSeats Left\t\t\tPrice");
                                            Log.d("jsonarray", jsonArray.toString());
                                            sourceid = new Integer[jsonArray.length()];
                                            dest = new Integer[jsonArray.length()];
                                            busno = new String[jsonArray.length()];
                                            sourcename = new String[jsonArray.length()];
                                            destname = new String[jsonArray.length()];
                                            arrtime = new String[jsonArray.length()];
                                            deptime = new String[jsonArray.length()];
                                            nopass = new Integer[jsonArray.length()];
                                            bus_id = new Integer[jsonArray.length()];
                                            priceeach = new Integer[jsonArray.length()];
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                bus_id[i] = jsonObject2.getInt("id");
                                                sourceid[i] = jsonObject2.getInt("src_id");
                                                dest[i] = jsonObject2.getInt("dest_id");
                                                sourcename[i] = jsonObject2.getString("src");
                                                //Log.d("sourcename",sourcename[i]);
                                                destname[i] = jsonObject2.getString("dest");
                                                busno[i] = jsonObject2.getString("bus_no");
                                                arrtime[i] = jsonObject2.getString("arr_time");
                                                deptime[i] = jsonObject2.getString("dep_time");
                                                nopass[i] = jsonObject2.getInt("seats");
                                                priceeach[i] = jsonObject2.getInt("price");
                                                buslist.add(busno[i] + "\t\t\t" + arrtime[i] + "\t\t\t\t" + deptime[i] + "\t\t\t\t\t\t" + nopass[i] + "\t\t\t\t\t\t\t\t\t\t" + "$"+priceeach[i]);
                                            }
                                            listadapter.notifyDataSetChanged();
                                            listadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                            getBusList.setAdapter(listadapter);
                                        }else{
                                            buses.setText("No Buses");
                                            buses.setGravity(Gravity.CENTER_HORIZONTAL);
                                        }
                                    } else {
                                            System.out.println("Did not receive the result for the backend for /getBuses");
                                        }
                                    }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(jsonObject);

            }}
        });
        getBusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoPayment(position);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        date.setText(year+"-"+String.format("%02d",month)+"-"+String.format("%02d",dayOfMonth));

                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }
    public void gotoPayment(int i){


            Intent intent = new Intent(this, Payment.class);
            intent.putExtra("source", sourceid[i]);
            intent.putExtra("dest", dest[i]);
            intent.putExtra("sourcename", sourcename[i]);
            intent.putExtra("destname", destname[i]);
            intent.putExtra("arrtime", arrtime[i]);
            intent.putExtra("deptime", deptime[i]);
            intent.putExtra("busno", busno[i]);
            intent.putExtra("nopass", nopass[i]);
            intent.putExtra("passengers", passengers);
            intent.putExtra("user_id", user_id);
            intent.putExtra("price", priceeach[i]);
            intent.putExtra("bus_id", bus_id[i]);
            intent.putExtra("date", date.getText().toString());
            startActivity(intent);

    }
}
