package com.example.tourismcanada;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class GetAPIRequest {
    private Context mContext;

    public void request(final Context context, final FetchDataListener listener, final String ApiURL) throws JSONException {
        mContext = context;
        if (listener != null) {
            //call onFetchStart of the listener to show progress dialog
            listener.onFetchStart();
        }
        //base server URL
        String baseUrl="https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev/";
        String url = baseUrl + ApiURL;
        Log.d("Gaurav: ", url);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Gaurav: ", response.toString());
                            if (listener != null) {
                                if(response.has("items")) {
                                    //received response
                                    listener.onFetchComplete(response);
                                }else if(response.has("error")){
                                    //call onFetchFailure of the listener
                                    listener.onFetchFailure(response.getString("error"));
                                } else{
                                    listener.onFetchComplete(null);
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Gaurav:", error.toString());
                if (error instanceof NoConnectionError) {
                    listener.onFetchFailure("Network Connectivity Problem");
                } else if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volley_error = new VolleyError(new String(error.networkResponse.data));
                    String errorMessage = "";
                    try {
                        JSONObject errorJson = new JSONObject(volley_error.getMessage().toString());
                        if(errorJson.has("error")) errorMessage = errorJson.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (errorMessage.isEmpty()) {
                        errorMessage = volley_error.getMessage();
                    }
                    if (listener != null) listener.onFetchFailure(errorMessage);
                } else {
                    listener.onFetchFailure("Please try again.");
                }
            }
        });
        RequestQueueService.getInstance(context).addToRequestQueue(postRequest.setShouldCache(false));
    }
}
