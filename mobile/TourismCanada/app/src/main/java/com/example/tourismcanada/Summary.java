package com.example.tourismcanada;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourismcanada.R;

public class Summary extends AppCompatActivity {
    TextView summary;
    String travel_date,user,booking_date,source,dest,bus_no,arr_time,dep_time;
    int num_pass,unit_price,total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        summary=(TextView) findViewById(R.id.invoiceID);
        Intent intent=getIntent();
        travel_date=intent.getStringExtra("travel_date");
        user=intent.getStringExtra("user");
        booking_date=intent.getStringExtra("booking_date");
        source=intent.getStringExtra("source");
        dest=intent.getStringExtra("destination");
        bus_no=intent.getStringExtra("busno");
        arr_time=intent.getStringExtra("arr_time");
        dep_time=intent.getStringExtra("dep_time");
        num_pass=intent.getIntExtra("num_pass",0);
        unit_price=intent.getIntExtra("unit_price",0);
        total=intent.getIntExtra("total",0);
//        Toast.makeText(this,travel_date+" "+user+" "+booking_date+" "+source+" "+dest+" "+bus_no+" "+arr_time+" "+dep_time+" "+num_pass+" "+unit_price+" "+total,Toast.LENGTH_LONG).show();
        String details="Travel date:           "+travel_date+"\nUser:                       "+user+"\nBooking date:         "+booking_date+"\nSource:               "+source+"\nDestination:           "+dest+"\nBus No.:                 "+bus_no+"\nArrival Time:          "+arr_time+"\nDeparture Time:    "+dep_time+"\nSeats Booked:       "+num_pass+"\nPrice:                      $"+unit_price+"\nTotal:                      $"+total;
        summary.setText(details);
    }
}