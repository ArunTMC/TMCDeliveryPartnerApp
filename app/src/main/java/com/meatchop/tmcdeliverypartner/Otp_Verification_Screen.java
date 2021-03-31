package com.meatchop.tmcdeliverypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Otp_Verification_Screen extends AppCompatActivity {
    private EditText edOtp1, edOtp2, edOtp3, edOtp4, edOtp5, edOtp6;
    private static final String TAG = "TAG";
    private String passCode, userMobileString,vendorKey,deliveryPartnerKey;
    Button verifyOtpButton;
    private TextView userPhoneNumberTextview;
    private static final int REQ_USER_CONSENT = 2;
    Boolean isUserAlreadyAddedinDB ;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay;;
    LinearLayout loadingPanel,loadingpanelmask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp__verification__screen);
        edOtp1 = findViewById(R.id.otp_first_et);
        edOtp2 = findViewById(R.id.otp_second_et);
        edOtp3 = findViewById(R.id.otp_third_et);
        edOtp4 = findViewById(R.id.otp_fourth_et);
        edOtp6 = findViewById(R.id.otp_sixth_et);
        edOtp5 = findViewById(R.id.otp_fifth_et);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);

        verifyOtpButton = findViewById(R.id.verifyOtp_button);

        userPhoneNumberTextview = findViewById(R.id.userPhoneNumberTextview);
        GetUserPhoneNumber();
        //RegisterBroadcastReceiver();
        //StartSmsUserConsent();

        SharedPreferences shared = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));

        edOtp1.addTextChangedListener(new GenericTextWatcher(edOtp1));
        edOtp2.addTextChangedListener(new GenericTextWatcher(edOtp2));
        edOtp3.addTextChangedListener(new GenericTextWatcher(edOtp3));
        edOtp4.addTextChangedListener(new GenericTextWatcher(edOtp4));
        edOtp5.addTextChangedListener(new GenericTextWatcher(edOtp5));
        edOtp6.addTextChangedListener(new GenericTextWatcher(edOtp6));


        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNewUserOrNot(userMobileString);
                saveUserDetails();

                Intent i= new Intent(Otp_Verification_Screen.this, Dashboard.class);
                startActivity(i);
                finish();
                if ((edOtp1.getText().toString().length() != 0) && (edOtp2.getText().toString().length() != 0) && (edOtp3.getText().toString().length() != 0) && (edOtp4.getText().toString().length() != 0) && (edOtp5.getText().toString().length() != 0) && (edOtp6.getText().toString().length() != 0)) {
                    passCode = edOtp1.getText().toString().trim() + edOtp2.getText().toString().trim() + edOtp3.getText().toString().trim() + edOtp4.getText().toString().trim() + edOtp5.getText().toString().trim() + edOtp6.getText().toString().trim();
                    Adjusting_Widgets_Visibility(true);

                  //  verifyotp();
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

    /*

        private void StartSmsUserConsent() {
            SmsRetrieverClient client = SmsRetriever.getClient(this);
            //We can add sender phone number or leave it blank
            // I'm adding null here
            client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
                }
            });
        }
        private void RegisterBroadcastReceiver() {
            otpBroadcastReceiver = new OtpBroadcastReceiver();
            otpBroadcastReceiver.smsBroadcastReceiverListener =
                    new OtpBroadcastReceiver.SmsBroadcastReceiverListener() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, REQ_USER_CONSENT);
                        }

                        @Override
                        public void onFailure() {

                        }
                    };
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            registerReceiver(otpBroadcastReceiver, intentFilter);
        }
       /* @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQ_USER_CONSENT) {
                if ((resultCode == RESULT_OK) && (data != null)) {
                    //That gives all message to us.
                    // We need to get the code from inside with regex
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    // textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));

                    getOtpFromMessage(message);
                    unregisterReceiver(otpBroadcastReceiver);

                }
            }
        }


        private void getOtpFromMessage(String message) {
            // This will match any 6 digit number in the message
            Pattern pattern = Pattern.compile("(|^)\\d{6}");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                edOtp1.setText(matcher.group(0));

            }
        }

        */
    private void GetUserPhoneNumber() {
        userMobileString = getIntent().getStringExtra("phone");
        userPhoneNumberTextview.setText(userMobileString);
    }

    class GenericTextWatcher implements TextWatcher {
        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
            Log.i("Tag", "ONGeneric Text Watcher");
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            Log.i("Tag", "ONAfterTextChanged");
            switch (view.getId()) {

                case R.id.otp_first_et:

                    if (text.length() == 2) {
                        edOtp1.setText(String.valueOf(text.charAt(0)));
                        edOtp2.setText(String.valueOf(text.charAt(1)));
                        edOtp2.requestFocus();
                        edOtp2.setSelection(edOtp2.getText().length());
                    } else if (text.length() == 6) {
                        edOtp1.setText(String.valueOf(text.charAt(0)));
                        edOtp2.setText(String.valueOf(text.charAt(1)));
                        edOtp3.setText(String.valueOf(text.charAt(2)));
                        edOtp4.setText(String.valueOf(text.charAt(3)));
                        edOtp5.setText(String.valueOf(text.charAt(4)));
                        edOtp6.setText(String.valueOf(text.charAt(5)));

                        edOtp6.requestFocus();
                        edOtp6.setSelection(edOtp3.getText().length());
                    }

                    break;
                case R.id.otp_second_et:

                    if (text.length() > 1) {
                        edOtp2.setText(String.valueOf(text.charAt(0)));
                        edOtp3.setText(String.valueOf(text.charAt(1)));
                        edOtp3.requestFocus();
                        edOtp3.setSelection(edOtp3.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp1.requestFocus();
                        edOtp1.setSelection(edOtp1.getText().length());
                    }
                    break;
                case R.id.otp_third_et:

                    if (text.length() > 1) {
                        edOtp3.setText(String.valueOf(text.charAt(0)));
                        edOtp4.setText(String.valueOf(text.charAt(1)));
                        edOtp4.requestFocus();
                        edOtp4.setSelection(edOtp4.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp2.requestFocus();
                        edOtp2.setSelection(edOtp2.getText().length());
                    }
                    break;

                case R.id.otp_fourth_et:

                    if (text.length() > 1) {
                        edOtp4.setText(String.valueOf(text.charAt(0)));
                        edOtp5.setText(String.valueOf(text.charAt(1)));
                        edOtp5.requestFocus();
                        edOtp5.setSelection(edOtp5.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp3.requestFocus();
                        edOtp3.setSelection(edOtp3.getText().length());
                    }
                    break;

                case R.id.otp_fifth_et:

                    if (text.length() > 1) {
                        edOtp5.setText(String.valueOf(text.charAt(0)));
                        edOtp6.setText(String.valueOf(text.charAt(1)));
                        edOtp6.requestFocus();
                        edOtp6.setSelection(edOtp6.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp4.requestFocus();
                        edOtp4.setSelection(edOtp4.getText().length());
                    }
                    break;

                case R.id.otp_sixth_et:
                    if (text.length() == 0) {
                        edOtp5.requestFocus();
                        edOtp5.setSelection(edOtp5.getText().length());
                    }
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            Log.i("Tag", "ONbeforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            Log.i("Tag", "ONTextChanged");
        }
    }


    private void verifyotp() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("ANSWER", passCode);
        Log.d(TAG, "Si " + passCode);

        AWSMobileClient.getInstance().confirmSignIn(attributes, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult signInResult) {
                Log.d(TAG, "UserName " + userMobileString + "  password " + passCode);

                Log.d(TAG, "Log-in  state: " + signInResult.getSignInState());
                switch (signInResult.getSignInState()) {
                    case DONE:
                        Log.d(TAG, "Log-in done");
                        checkNewUserOrNot(userMobileString);
                        saveUserDetails();

                        Intent i= new Intent(Otp_Verification_Screen.this, Dashboard.class);
                       startActivity(i);
                       finish();
                        break;
                    case SMS_MFA:
                        Log.d(TAG, "Please confirm sign-in with SMS.");
                        break;
                    case NEW_PASSWORD_REQUIRED:
                        Log.d(TAG, "Please confirm sign-in with new password.");
                        break;
                    case CUSTOM_CHALLENGE:
                        Adjusting_Widgets_Visibility(false);

                        AlertDialogClass.showDialog(Otp_Verification_Screen.this, R.string.Enter_Correct_OTP);

                        Log.d(TAG, " Custom challenge.");
                        break;
                    default:
                        Log.d(TAG, "Unsupported Log-in confirmation: " + signInResult.getSignInState().toString());
                        break;
                }
            }

            @Override
            public void onError(final Exception e) {

                Adjusting_Widgets_Visibility(false);

                Log.d(TAG, "UserNamexx" + userMobileString + "  passwordvv" + passCode);

                Log.e(TAG, "Log-in error", e);
            }
        });
    }
    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
    }


    private void uploaduserDatatoDB() {
        String createdtime = getDate_and_time();
    if(isUserAlreadyAddedinDB){
        Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Available");
            jsonObject.put("key", deliveryPartnerKey);
            jsonObject.put("vendorkey", vendorKey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateDeliveryUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    else
    {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "NewExecutive");
            jsonObject.put("vendorkey", vendorKey);
            jsonObject.put("status", "Available");
            jsonObject.put("mobileno", userMobileString);
            jsonObject.put("createdtime", createdtime);
            jsonObject.put("updatedtime", createdtime);
            jsonObject.put("authorizationcode", "001");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addDeliveryUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try {

                   JSONArray result  = response.getJSONArray("content");
                    Log.d(Constants.TAG, "Response: " + result);
                    int i1=0;
                    int arrayLength = result.length();
                    Log.d("Constants.TAG", "Response: " + arrayLength);


                    for(;i1<=(arrayLength-1);i1++) {

                        try {
                            JSONObject json = result.getJSONObject(i1);

                            deliveryPartnerKey = String.valueOf(json.get("key"));
                            Log.d(Constants.TAG, "deliveryPartnerKey: " + deliveryPartnerKey);
                            saveUserDetails();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                            Log.d(Constants.TAG, "e: " + e.getMessage());
                            Log.d(Constants.TAG, "e: " + e.toString());

                        }
                        Adjusting_Widgets_Visibility(false);

                        Intent i = new Intent(Otp_Verification_Screen.this, Dashboard.class);
                        startActivity(i);
                        Log.d(Constants.TAG, "Response: " + response);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }

            }


            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    }

    private Boolean checkNewUserOrNot(String userMobileString) {
        Log.d(Constants.TAG, "checkNewUserOrNot: " );
        String userMobileStringEncoded  = userMobileString;
        try {
             userMobileStringEncoded = URLEncoder.encode(userMobileString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryUserforMobileNo+"?mobileno="+userMobileStringEncoded,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                Log.d(Constants.TAG, "Response of checkNewUserOrNot: " + response);
                try {
                    String content = response.getString("content");
                    if(content.equals("Not Available")){
                        saveUserDetails();

                        isUserAlreadyAddedinDB=false;
                        uploaduserDatatoDB();

                    }
                    if(content.equals("Available")){
                        JSONArray resultarray = response.getJSONArray("result");
                        for(int i=0;i<resultarray.length();i++){
                            JSONObject jsonObject = resultarray.getJSONObject(i);
                            deliveryPartnerKey = String.valueOf(jsonObject.get("key"));

                        }
                        saveUserDetails();

                        isUserAlreadyAddedinDB=true;
                        uploaduserDatatoDB();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        return true;
    }

    private void saveUserDetails() {


        SharedPreferences sharedPreferences
                = getSharedPreferences("LoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();


        myEdit.putString(
                "deliveryUserkey", deliveryPartnerKey
        );
        myEdit.putBoolean(
                "DeliveryUserLoginStatus",
                true);


        myEdit.apply();

    }


}
