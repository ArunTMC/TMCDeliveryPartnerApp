package com.meatchop.tmcdeliverypartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class Settings extends AppCompatActivity {
    Spinner deliveryPartnerStatusChanging_Spinner;
    List<String> statusChangingSpinnerData;
    String vendorKey,deliveryUserMobileNumber,deliveryUserkey,Status,deliveryPartnerName;
    TextView deliveryUserStatus,userMobileNo,deliverypersonName;
    LinearLayout logout,dashboard_layout,stopSharingLocation,deliveredOrdersLayout;
    Button changeName_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        deliveryPartnerStatusChanging_Spinner = findViewById(R.id.deliveryPartnerStatusChanging_Spinner);
        deliveryUserStatus = findViewById(R.id.deliveryUserStatus);
        userMobileNo = findViewById(R.id.userMobileNo);
        dashboard_layout = findViewById(R.id.dashboard_layout);
        stopSharingLocation = findViewById(R.id.stopSharingLocation);
        changeName_button =findViewById(R.id.changeName_button);
        deliveredOrdersLayout = findViewById(R.id.deliveredOrdersLayout);
        logout = findViewById(R.id.logout);
        deliverypersonName = findViewById(R.id.deliveryPersonname_textwidget);
        statusChangingSpinnerData = new ArrayList<>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    SharedPreferences shared = getApplicationContext().getSharedPreferences("LoginData", MODE_PRIVATE);
                    vendorKey = (shared.getString("VendorKey", ""));
                    deliveryUserMobileNumber = (shared.getString("deliveryUserMobileNumber", ""));
                    userMobileNo.setText(deliveryUserMobileNumber);

                    Status = (shared.getString("deliveryUserStatus", "AVAILABLE"));
                    deliveryUserStatus.setText(Status);
                    deliveryUserkey = (shared.getString("deliveryUserkey", ""));


                    getDeliveryPartnerName(deliveryUserkey);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        setDataForSpinner();

        deliveryPartnerStatusChanging_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
                String Status = parent.getItemAtPosition(position).toString().toUpperCase();
                deliveryUserStatus.setText(Status);
                saveStatusinLocal(Status);
                Toast.makeText(parent.getContext(), "Selected: " + Status,          Toast.LENGTH_LONG).show();
                ChangeStatusofTheDeliveryPartner(deliveryUserkey,Status);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        stopSharingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard dashboard =new Dashboard();
                dashboard.closeService();
            }
        });
        dashboard_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this,Dashboard.class);
                startActivity(i);
                overridePendingTransition(0, 0);

                finish();
            }
        });
        deliveredOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this,DeliveredOrdersDatewise.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOutfromAWSandClearSharedPref();
            }
        });

        changeName_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Dialog dialog = new Dialog(Settings.this);
                            dialog.setContentView(R.layout.changedelivvery_partner_name_dialog);

                            dialog.setCanceledOnTouchOutside(true);

                            EditText dialog_deliverypersonName =  dialog.findViewById(R.id.deliverypersonName);
                            Button dialog_save_name =  dialog.findViewById(R.id.save_name);

                            dialog_save_name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(dialog_deliverypersonName.getText().toString().length()>0){
                                        deliveryPartnerName = dialog_deliverypersonName.getText().toString();
                                        changeNameofDeliveryPartner(deliveryUserkey,deliveryPartnerName);
                                      //  getDeliveryPartnerName(deliveryUserkey);
                                        dialog.cancel();

                                    }
                                }
                            });






                            dialog.show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });



    }

    private void getDeliveryPartnerName(String deliveryUserkey) {



        Log.d(Constants.TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", deliveryPartnerName);
            jsonObject.put("key", deliveryUserkey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getdeliveryPartnerName+"?key="+deliveryUserkey+"&tablename=DeliveryUser",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject jsonObjectResponse) {
                try {

                    JSONObject content  = jsonObjectResponse.getJSONObject("content");
                    JSONObject result = content.getJSONObject("Item");
                    Log.d(Constants.TAG, "Response: " + result);

                    int i1=0;
                    int arrayLength = result.length();
                    Log.d(Constants.TAG, "Response: " + arrayLength);


                            String Name = result.getString("name");
                    Log.d(Constants.TAG, "name: " + Name);

                    deliverypersonName.setText(Name);


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


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);





    }

    private void changeNameofDeliveryPartner(String deliveryUserkey, String deliveryPartnerName) {



        Log.d(Constants.TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", deliveryPartnerName);
            jsonObject.put("key", deliveryUserkey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateDeliveryUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject jsonObjectResponse) {
                try {
                    String result  = jsonObjectResponse.getString("message");
                    if(result.equals("success")){
                        Log.d(Constants.TAG, "Succesfully changed the status of the delivery partner: " );
                        Toast.makeText(Settings.this,"Your Name has Changed into "+ deliveryPartnerName,Toast.LENGTH_SHORT).show();
                        getDeliveryPartnerName(deliveryUserkey);

                    }
                    else{
                        Toast.makeText(Settings.this,"error in changing your name ",Toast.LENGTH_SHORT).show();

                        Log.d(Constants.TAG, "Error while changing the status of the delivery partner: " );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Settings.this,"Error ",Toast.LENGTH_SHORT).show();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Settings.this,"Error ",Toast.LENGTH_SHORT).show();

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

    private void signOutfromAWSandClearSharedPref() {

        AWSMobileClient.getInstance().signOut();

        SharedPreferences sharedPreferences
                = getSharedPreferences("LoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

        myEdit.putString(
                "deliveryUserMobileNumber",
                "");

        myEdit.putString(
                "VendorKey",
                "");
        myEdit.putString(
                "VendorName",
                ""
        );
        myEdit.putBoolean(
                "DeliveryUserLoginStatus",
                false);


        myEdit.apply();

        Intent i = new Intent(Settings.this, Login_Screen.class);
        startActivity(i);




    }
    private void saveStatusinLocal(String status) {
        SharedPreferences sharedPreferences
                = getSharedPreferences("LoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

        myEdit.putString(
                "deliveryUserStatus",
                status);
        myEdit.apply();
    }


    private void ChangeStatusofTheDeliveryPartner(String deliveryUserkey, String status) {

        Log.d(Constants.TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("key", deliveryUserkey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateDeliveryUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject jsonObjectResponse) {
                try {
                    String result  = jsonObjectResponse.getString("message");
                    if(result.equals("success")){
                        Log.d(Constants.TAG, "Succesfully changed the status of the delivery partner: " );

                    }
                    else{
                        Log.d(Constants.TAG, "Error while changing the status of the delivery partner: " );

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void setDataForSpinner() {

        statusChangingSpinnerData.add("AVAILABLE");
        statusChangingSpinnerData.add("NOT AVAILABLE");
        statusChangingSpinnerData.add("BUSY");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, statusChangingSpinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryPartnerStatusChanging_Spinner.setAdapter(arrayAdapter);
        if(Status.toUpperCase().equals("AVAILABLE")){
            deliveryPartnerStatusChanging_Spinner.setSelection(0);
        }
        if(Status.toUpperCase().equals("NOT AVAILABLE")){
            deliveryPartnerStatusChanging_Spinner.setSelection(1);

        }
        if(Status.toUpperCase().equals("BUSY")){
            deliveryPartnerStatusChanging_Spinner.setSelection(2);

        }

    }
}