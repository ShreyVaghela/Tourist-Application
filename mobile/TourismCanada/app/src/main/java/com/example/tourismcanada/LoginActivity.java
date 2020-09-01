package com.example.tourismcanada;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button Login;
    TextView Reg;
    final Context context = this;
    String code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.login_bttn);
        Reg = findViewById(R.id.reg);
        final String TAG = "LoginActivity";

        final AuthenticationHandler authentication = new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

                Log.i(TAG, "Login Successful");
                Log.i(TAG, String.valueOf(userSession));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("login_button", "hide");
                bundle.putString("user_id",email.getText().toString());
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                String email_addr = email.getText().toString();
                String pass = password.getText().toString();

                Log.i(TAG, "in getAuthenticationDetails()...");
                //getting email_id and password
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId,pass,null);

                //providing credentials to continuation
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                //continue sign in
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(final MultiFactorAuthenticationContinuation continuation) {

               Log.i(TAG, "in mfa()...");

                // get mfa_verification_code.xml view
                LayoutInflater lin = LayoutInflater.from(context);
                View mfa_verif_view = lin.inflate(R.layout.mfa_verification_code, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                // set mfa_verification_code.xml to alert
                alert.setView(mfa_verif_view);

                final EditText mfa_code = (EditText) mfa_verif_view.findViewById(R.id.ver_code);

                // set dialog message
                alert.setCancelable(false).setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        code = mfa_code.getText().toString();
                                       // Log.i(TAG, "in dialog()..."+code+": "+mfa_code);
                                        continuation.setMfaCode(code);
                                        continuation.continueTask();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialogBox = alert.create();

                // show it
                alertDialogBox.show();
              //  Log.i(TAG, "in mfa()..."+code);
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "Login failed: "+exception.getLocalizedMessage());
                Toast.makeText(LoginActivity.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_addr = email.getText().toString();
                String pass = password.getText().toString();

                if(email_addr.equals("")){
                    Toast.makeText(LoginActivity.this, "Please Enter Email address", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals("")){
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else {

                    AwsCognitoConfig cognitoConfig = new AwsCognitoConfig(LoginActivity.this);

                    CognitoUser user = cognitoConfig.getPool().getUser(email_addr);
                    user.getSessionInBackground(authentication);
                }
            }
        });

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
