package com.example.tourismcanada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    TextView totalPrice;
    EditText cardNoEditText;
    String cardNo;
    EditText expiryDateEditText;
    String expiryDate;
    EditText cvvEditText;
    String cvv;
    Button paymentButton;
    String date;
    private RequestQueue queue;

    String baseURL = "https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev",user_id;
    int passengers, price, source_id, dest_id, bus_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Intent intent = getIntent();
        passengers = intent.getIntExtra("passengers", 0);
        user_id = intent.getStringExtra("user_id");
        price = intent.getIntExtra("price", 0);
        source_id = intent.getIntExtra("source", 0);
        dest_id = intent.getIntExtra("dest", 0);
        bus_id = intent.getIntExtra("bus_id", 0);
        date = intent.getStringExtra("date");
        paymentButton = (Button) findViewById(R.id.paymentButtonID);
        cardNoEditText = (EditText) findViewById(R.id.cardNoID);
        expiryDateEditText = (EditText) findViewById(R.id.expiryDateID);
        cvvEditText = (EditText) findViewById(R.id.cvvID);
        totalPrice = (TextView) findViewById(R.id.totalPriceID);
        totalPrice.setText("$"+Integer.toString(passengers * price));
        queue = Volley.newRequestQueue(this);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNo = cardNoEditText.getText().toString();
                expiryDate = expiryDateEditText.getText().toString();
                cvv = cvvEditText.getText().toString();
                //Toast toast=Toast.makeText(getApplicationContext(),cardNo+" "+expiryDate+" "+cvv+"  "+passengers,Toast.LENGTH_LONG);
                //toast.show();
                goToPayment();
            }
        });
    }

    public void goToPayment() {
        String URL = baseURL + "/mobileMakePayment";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(payment.this, response, Toast.LENGTH_LONG).show();
                        try{
                            JSONArray rows=new JSONArray(response);
                            JSONArray results=rows.getJSONArray(0);
                            Intent intent =new Intent(Payment.this, Summary.class);
                            intent.putExtra("travel_date",date);
                            System.out.println(results.getString(0));
                            intent.putExtra("user",results.getString(1));
                            intent.putExtra("booking_date",results.getString(2));
                            intent.putExtra("source",results.getString(3));
                            intent.putExtra("destination",results.getString(4));
                            intent.putExtra("num_pass",results.getInt(5));
                            intent.putExtra("busno",results.getString(6));
                            intent.putExtra("arr_time",results.getString(7));
                            intent.putExtra("dep_time",results.getString(8));
                            intent.putExtra("unit_price",results.getInt(9));
                            intent.putExtra("total",results.getInt(10));
                            startActivity(intent);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(Payment.this,"Payment failed. Please check your card details.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Payment.this,"Please try again.",Toast.LENGTH_LONG).show();
                        Toast.makeText(Payment.this,error.toString(),Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", user_id);
                params.put("source_id", String.valueOf(source_id));
                params.put("dest_id", String.valueOf(dest_id));
                params.put("bus_id", String.valueOf(bus_id));
                params.put("price", String.valueOf(price));
                params.put("date", date);
                params.put("numPass", String.valueOf(passengers));
                params.put("cardNumber", cardNo);
                params.put("cardName", "ShreyVaghela");
                params.put("expiryDate", expiryDate);
                params.put("cvCode", cvv);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
        queue.add(stringRequest);
//
    }
}