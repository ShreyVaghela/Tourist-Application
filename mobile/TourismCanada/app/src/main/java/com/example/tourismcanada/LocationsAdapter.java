package com.example.tourismcanada;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationsHolder> {
    private ArrayList<Location> locationList;
    private Context mContext;
    private String user_id;
    private boolean isAuthenticated;

    public LocationsAdapter(Context context, ArrayList<Location> locationList, String userid, boolean isAuthenticated) {
        mContext = context;
        this.locationList = locationList;
        this.user_id = userid;
        this.isAuthenticated = isAuthenticated;
    }

    @NonNull
    @Override
    public LocationsAdapter.LocationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.location_card_view, parent, false);
        return new LocationsHolder(view);
    }

    @Override
    public int getItemCount() {
        return locationList == null? 0: locationList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsAdapter.LocationsHolder holder, int position) {
        final Location location = locationList.get(position);
        holder.setLocationImage(location.getImage_url());
        holder.setLocationDescription(location.getDescription());
        holder.setLocationHighlights(location.getHighlights());
        holder.setLocationName(location.getName());
        holder.setLocationPrice(location.getPrice());
        holder.setLocationItemClickListener(new LocationItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if( isAuthenticated == false || user_id.length()==0){
                    mContext.startActivity(new Intent(mContext.getApplicationContext(),LoginActivity.class));
                }
                else {
                    Intent intent = new Intent(mContext.getApplicationContext(), Booking.class);
                    intent.putExtra("destId", location.getAddress_id());
                    intent.putExtra("user_id", user_id);
                    Log.d("user id: and dest_id: ",user_id + " " + location.getAddress_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public class LocationsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView locationImage;
        private TextView locationName;
        private TextView locationDescription;
        private TextView locationHighlights;

        private TextView locationPrice;
        private LocationItemClickListener locationItemClickListener;

        @Override
        public void onClick(View v) {
            locationItemClickListener.onItemClick(v, getLayoutPosition());
        }

        private void setLocationItemClickListener(LocationItemClickListener listener) {
            locationItemClickListener = listener;
        }

        public LocationsHolder(@NonNull final View itemView) {
            super(itemView);
            locationImage = itemView.findViewById(R.id.location_image);
            locationName = itemView.findViewById(R.id.location_name);
            locationDescription = itemView.findViewById(R.id.location_description);
            locationHighlights = itemView.findViewById(R.id.location_highlights);
            locationPrice = itemView.findViewById(R.id.location_price);
            itemView.setOnClickListener(this);
        }

        public void setLocationHighlights(String highlights) {
            locationHighlights.setText(highlights);
        }

        public void setLocationImage(String url) {
            new ImageLoadTask(url, locationImage).execute();
        }

        public void setLocationName(String name) {
            locationName.setText(name);
        }

        public void setLocationDescription(String description) {
            locationDescription.setText(description);
        }

        public void setLocationPrice(String price) {
            locationPrice.setText(price);
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}
