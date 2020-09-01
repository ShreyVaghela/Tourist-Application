package com.example.tourismcanada;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {

    private ArrayList<Order> orderList;

    public OrdersAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_card_view, parent, false);
        return new OrdersAdapter.OrdersHolder(view);
    }

    @Override
    public int getItemCount() {
        return orderList == null? 0: orderList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrdersHolder holder, int position) {
        final Order order = orderList.get(position);
        holder.setOrderAddress(order.getFrom_to_address());
        holder.setTime(order.getTime());
        holder.setPassengers(order.getNum_passengers());
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {
        private TextView from_to_address;
        private TextView timeView;
        private TextView num_passengers;

        public OrdersHolder(@NonNull View itemView) {
            super(itemView);
            from_to_address = itemView.findViewById(R.id.order_from_to);
            timeView = itemView.findViewById(R.id.order_date_time);
            num_passengers = itemView.findViewById(R.id.order_passengers);
        }

        public void setOrderAddress(String address) {
            from_to_address.setText(address);
        }

        public void setTime(String time) {
            timeView.setText("Journey Date: "+ time);
        }

        public void setPassengers(int passengers) {
            num_passengers.setText("Passengers: "+ passengers);
        }
    }
}