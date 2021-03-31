package com.meatchop.tmcdeliverypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Login_Screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText login_mobileNo_Text;
    private String mobileNo_String;
    private boolean DeliveryUserLoginStatusBoolean;
    Spinner vendorName_Spinner;
    private String mobile_vendorLatitude;
    private String mobile_vendorLongitude;


    private String mobile_vendorNameString;
    private String mobile_password;
    private String mobile_userPhoneNumber;
    private String mobile_vendorKey;
    private String mobile_vendorMobileNumber;
    private String mobile_vendorAddressline1,mobile_vendorAddressline2,mobile_vendorPincode;
    private String mobile_vendorStatus, mobile_vendorFssaino;
    LinearLayout loadingPanel,loadingpanelmask;

    private ArrayAdapter mobile_spinner_aAdapter;
    private ArrayList<String> VendorName_arrayList;
    private JSONArray result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        findViewById(R.id.sendOtp_button);
        Button sendOtp_button;
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);

        login_mobileNo_Text = findViewById(R.id.login_mobileNo_Text);
        sendOtp_button = findViewById(R.id.sendOtp_button);
        vendorName_Spinner = findViewById(R.id.vendorName_Spinner);
        VendorName_arrayList = new ArrayList<String>();

             vendorName_Spinner.setOnItemSelectedListener(this);
        SharedPreferences sharedPreferences
                = getSharedPreferences("LoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

        myEdit.putString(
                "deliveryUserStatus",
                "AVAILABLE");
        myEdit.apply();



        sendOtp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_userPhoneNumber = login_mobileNo_Text.getText().toString().trim();
                if(mobile_userPhoneNumber .length() == 10) {
                    mobile_userPhoneNumber = "+91" + mobile_userPhoneNumber;
                    Adjusting_Widgets_Visibility(true);

                    //saveVendorLoginStatus();

                    StartSignUp(mobile_userPhoneNumber);
                }
                else{

                    // showDVAlert(R.string.Enter_Correct_No);

                    AlertDialogClass.showDialog(Login_Screen.this,R.string.Enter_Correct_No);
                }
            }
        });
    }



    private void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AWSMobileIntialization();
      //  Adjusting_Widgets_Visibility(true);

    }

    private void AWSMobileIntialization() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i("Tag", "Intialization Success");

                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                        if (userStateDetails.getUserState() == UserState.SIGNED_IN) {
                            Log.i("Tag", "Intialization Success 2");

                            // loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                            //  loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);
                            SharedPreferences sh
                                    = getSharedPreferences("VendorLoginData",
                                    MODE_PRIVATE);

                            DeliveryUserLoginStatusBoolean = sh.getBoolean("DeliveryUserLoginStatus", false);
                            Log.i("Tag", " Successfully got the vendor login status");

                            Log.i("Tag", "VendorLoginStatus ee" + DeliveryUserLoginStatusBoolean);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    Intent i;
                                    if (DeliveryUserLoginStatusBoolean) {
                                        Log.i("Tag", " Navigate to dashboard according to vendor login status");

                                        loadingPanel.setVisibility(View.INVISIBLE);
                                        loadingpanelmask.setVisibility(View.INVISIBLE);
                                        i = new Intent(Login_Screen.this, Dashboard.class);
                                    } else {
                                        Log.i("Tag", " Navigate to vendor selection screen according to vendor login status");

                                        loadingPanel.setVisibility(View.INVISIBLE);
                                        loadingpanelmask.setVisibility(View.INVISIBLE);
                                        i = new Intent(Login_Screen.this, Dashboard.class);
                                    }
                                    startActivity(i);

                                    finish();
                                }

                            });
                        } else {
                            Log.i("Tag", "First Signin bcz ur account is   " + userStateDetails.getUserState());
                            loadingPanel.setVisibility(View.INVISIBLE);
                            loadingpanelmask.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Tag", "Intialization not Success");
                        loadingPanel.setVisibility(View.INVISIBLE);
                        loadingpanelmask.setVisibility(View.INVISIBLE);
                        Log.e("INIT", "Initialization error.", e.fillInStackTrace());
                    }
                }
        );
            }

        });
    }



    private void StartSignUp(final String mobileNo_String) {

        final Map<String, String> attributes = new HashMap<>();
        attributes.put("phone_number", mobileNo_String);
        attributes.put("name", "arun");
        attributes.put("email", "");
        AWSMobileClient.getInstance().signUp(mobileNo_String, "password", attributes, null, new Callback<SignUpResult>() {
            @Override
            public void onResult(final SignUpResult signUpResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                        if (!signUpResult.getConfirmationState()) {
                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                            Log.d(TAG, "Sign-up callback state: " + details.getDestination());
                        } else {

                            StartSignIN(mobileNo_String);
                            Log.d(TAG, "Sign-up  " );
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //AlertDialogClass.showDialog(Login.this, R.string.Enter_Correct_No);
                        StartSignIN(mobileNo_String);
                    }
                });
                Log.e(TAG, "Sign-up error", e);
            }
        });

    }



    private void StartSignIN(final String mobileNo_String) {
        AWSMobileClient.getInstance().signIn(mobileNo_String, "password", null, new Callback<SignInResult>() {

            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "UserName"+mobileNo_String);

                        Log.d(TAG, "Log-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                Log.d(TAG,"Log-in done.");
                                Log.d(TAG,"Email: " + signInResult.getCodeDetails());
                                break;
                            case SMS_MFA:
                                Log.d(TAG,"Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                Log.d(TAG,"Please confirm sign-in with new password.");
                                break;
                            case CUSTOM_CHALLENGE:
                                saveVendorLoginStatus();
                                Log.d(TAG," Custom challenge.");
                                Adjusting_Widgets_Visibility(false);

                                Intent i = new Intent(Login_Screen.this, Otp_Verification_Screen.class);
                                Bundle b = new Bundle();
                                b.putString("phone",mobileNo_String);
                                i.putExtras(b);
                                startActivity(i);
                                break;
                            default:

                                Log.d(TAG,"Unsupported Log-in confirmation: " + signInResult.getSignInState());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                Log.e(TAG, "Log-in error", e);
            }
        });
    }

    private void getAreawiseVendorName() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofVendors +"?modulename=Store",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            result  = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    mobile_vendorNameString = String.valueOf(json.get("name"));
                                    mobile_vendorKey = String.valueOf(json.get("key"));
                                    Log.d(Constants.TAG, "JsonName: " + mobile_vendorNameString);

                                    if (!VendorName_arrayList.contains(mobile_vendorNameString)) {
                                        VendorName_arrayList.add(mobile_vendorNameString);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mobile_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
                        vendorName_Spinner.setAdapter(mobile_spinner_aAdapter);

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                return header;
            }
        };

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAreawiseVendorName();

    }




    //Doing the same with this method as we did with getName()
    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            JSONObject json = result.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mobile_vendorNameString=getVendorData(position,"name");
        mobile_vendorKey=getVendorData(position,"key");
        mobile_vendorMobileNumber=getVendorData(position,"vendormobile");
        mobile_vendorAddressline1=getVendorData(position,"addressline1");
        mobile_vendorAddressline2=getVendorData(position,"addressline2");
        mobile_vendorPincode=getVendorData(position,"pincode");
        mobile_vendorStatus=getVendorData(position,"status");
        mobile_vendorFssaino =getVendorData(position,"vendorfssaino");
        mobile_vendorLatitude =getVendorData(position,"locationlat");
        mobile_vendorLongitude =getVendorData(position,"locationlong");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void saveVendorLoginStatus() {
        SharedPreferences sharedPreferences
                = getSharedPreferences("LoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
      myEdit.putString(
                "deliveryUserMobileNumber",
                mobile_userPhoneNumber);

        myEdit.putString(
                "VendorKey",
                mobile_vendorKey);
        myEdit.putString(
                "VendorName",
                mobile_vendorNameString
        );

        myEdit.putString(
                "VendorLatitude",
                mobile_vendorLatitude
        );


        myEdit.putString(
                "VendorLongitute",
                mobile_vendorLongitude
        );


        myEdit.apply();

    }

    @Override
    public void onBackPressed() {
        new TMCAlertDialogClass(this, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        finish();
                    }

                    @Override
                    public void onNo() {

                    }
                });

    }

}