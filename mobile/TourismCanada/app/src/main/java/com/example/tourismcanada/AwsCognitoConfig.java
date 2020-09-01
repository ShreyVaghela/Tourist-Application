package com.example.tourismcanada;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class AwsCognitoConfig {

    private Context context;
    private String PoolId = "us-east-1_GOHU9cK0s";
    private String ClientId = "5g9n42bb7p629e99eil55180o9";
    private String clientSecret;
    private Regions Region = Regions.US_EAST_1;

    public AwsCognitoConfig(Context context){
        this.context=context;
    }
    public String getPoolId(){
        return PoolId;
    }

    public String getClientId(){
        return ClientId;
    }

    public Regions getRegion(){
        return Region;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public CognitoUserPool getPool(){
        return new CognitoUserPool(context, PoolId, ClientId,clientSecret, Region);
    }
}
