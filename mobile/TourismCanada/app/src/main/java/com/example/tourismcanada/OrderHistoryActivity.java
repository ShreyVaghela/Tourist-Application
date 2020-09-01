package com.example.tourismcanada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    private OrdersAdapter ordersAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    private TextView noOrderText;
    private String user_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        if (bundle != null) {
            user_id = bundle.getString("user_id");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.orders_recycler_view);
        noOrderText = findViewById(R.id.no_order_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ordersAdapter = new OrdersAdapter(orderArrayList);
        recyclerView.setAdapter(ordersAdapter);
        orderArrayList.clear();
        noOrderText.setVisibility(View.VISIBLE);
        //TODO: get current userID and pass it in the method
        getApiCall(user_id);
    }

    private void getApiCall(String userID){
        try{
            //Create Instance of GETAPIRequest and call it's
            //request() method
            GetAPIRequest getapiRequest=new GetAPIRequest();
            String url="orderdetails/"+userID;
            getapiRequest.request(OrderHistoryActivity.this, fetchSearchResultListener, url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Implementing interfaces of FetchDataListener for GET api request
    FetchDataListener fetchSearchResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Now check result sent by our GETAPIRequest class
                if (data != null) {
                    if (data.has("items")) {
                        JSONArray jsonArray = data.getJSONArray("items");
                        Log.d("Gaurav: ", " " + jsonArray.length());
                        if(jsonArray.length() > 0) {
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                int id = jobj.getInt("id");
                                String source = jobj.getString("source_id");
                                String destination = jobj.getString("dest_id");
                                int passengers = jobj.getInt("num_passengers");
                                String time = jobj.getString("date");
                                orderArrayList.add(new Order(id, source+"  ->  "+destination, time, passengers));
                            }
                            noOrderText.setVisibility(View.INVISIBLE);
                        } else {
                            noOrderText.setVisibility(View.VISIBLE);
                        }
                        ordersAdapter.notifyDataSetChanged();
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", OrderHistoryActivity.this);
                }
            }catch (Exception e){
                RequestQueueService.showAlert("Please try again.", OrderHistoryActivity.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg,OrderHistoryActivity.this);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(OrderHistoryActivity.this);
        }
    };
}
