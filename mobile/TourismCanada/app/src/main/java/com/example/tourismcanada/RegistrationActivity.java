package com.example.tourismcanada;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    Button button_date, button_login;
    EditText date, name, phone, email, password;
    RadioButton male,female;
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        button_date = findViewById(R.id.date_bttn);
        button_login = findViewById(R.id.login_bttn);
        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(day+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        final CognitoUserAttributes cUserAttr = new CognitoUserAttributes();

        final SignUpHandler signUpResponse = new SignUpHandler() {
            private static final String TAG = "MyActivity";
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                Log.i( TAG,"user registered "+signUpConfirmationState);

                if(!signUpConfirmationState){
                    Log.i(TAG,"user registered. verification link sent to email.."+
                            cognitoUserCodeDeliveryDetails.getDestination());
                    Toast.makeText(RegistrationActivity.this, "Sign Up successful, please check your email", Toast.LENGTH_SHORT).show();

                    saveUser();

                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Log.i(TAG, "user registered and confirmed..");
                }
            }

            @Override
            public void onFailure(Exception exception) {

                Log.i(TAG, "user registration failed: "+exception.getLocalizedMessage());
                Toast.makeText(RegistrationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_addr = email.getText().toString();
                String pass = password.getText().toString();
                String user_name = name.getText().toString();
                String ph_num = phone.getText().toString();
                String dob = date.getText().toString();
                String gender_male = male.getText().toString();
                String gender_female = female.getText().toString();

                String gender;
                if(gender_male.matches(""))
                {
                    gender = "female";
                }
                else{
                    gender = "male";
                }

                if(email_addr.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Email address", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if(user_name.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(ph_num.equals("") || !ph_num.startsWith("+1")){
                    Toast.makeText(RegistrationActivity.this, "Please Enter valid Mobile number", Toast.LENGTH_SHORT).show();
                }
                else if(dob.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Please Enter date of birth", Toast.LENGTH_SHORT).show();
                }
                else {
                    cUserAttr.addAttribute("name",user_name);
                    cUserAttr.addAttribute("phone_number",ph_num);
                    cUserAttr.addAttribute("gender",gender);
                    cUserAttr.addAttribute("custom:Birthday",dob);

                    String TAG = "MyActivity";
                    Log.i(TAG, user_name+","+ph_num+","+gender+","+dob+","+email_addr+","+pass);
                    AwsCognitoConfig cognitoConfig = new AwsCognitoConfig(RegistrationActivity.this);

                    cognitoConfig.getPool().signUpInBackground(email_addr,pass,cUserAttr,null, signUpResponse);
                }
            }
        });
    }

    public void saveUser() {
        String baseURL = "https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev";
        String URL = baseURL + "/registration/";
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("dob", "2020-03-18");
        String gender_male = male.getText().toString();
        String gender;
        if(gender_male.matches(""))
        {
            gender = "female";
        }
        else{
            gender = "male";
        }
        params.put("sex", gender);
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest(URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray rows=new JSONArray(response);
                            JSONArray results=rows.getJSONArray(0);

                        }catch (JSONException e){
//                            Toast.makeText(RegistrationActivity.this,"Reg failed. Please check your card details.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationActivity.this,"Check your email",Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        queue.add(stringRequest);
    }


}

