package com.probase.propay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.probase.propay.app.AppController;
import com.probase.propay.utils.Const;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends Activity implements View.OnClickListener {
    private String TAG = PaymentActivity.class.getSimpleName();
    private EditText mPin;
    private Button mPay;
    private String amount;
    private String merchantId;
    private ProgressDialog pDialog;
    private String stringPin;
    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //Retrieve params from bundle
             amount = extras.getString("amount");
             merchantId = extras.getString("merchantId");
            Log.d("HERE ", "" + amount);
            Log.d("HERE ", "" + merchantId);

        }

        mPin = (EditText) findViewById(R.id.etPin);
        mPay = (Button) findViewById(R.id.btPay);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        mPay.setOnClickListener(this);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq() {
        Map<String, String> params = new HashMap<>();
        stringPin = mPin.getText().toString();
        Log.d("AMOUNT", amount);
        Log.d("AMOUNT", stringPin);
        Log.d("AMOUNT", merchantId);
        params.put("pin",stringPin);
        params.put("amount", amount);
        params.put("merchantId", merchantId);
        showProgressDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_JSON_OBJECT, new JSONObject(params) ,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        //Toast.makeText(getApplicationContext(),"RESPONSE"+response.toString(),Toast.LENGTH_LONG).show();
                        messageResponse();
                        // msgResponse.setText(response.toString());
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
    public void messageResponse(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Payment SUCCESSFUL, Thank you for using ProPay");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onClick(View v) {
        Log.v("EditText", mPin.getText().toString());
        switch (v.getId()){
            case R.id.btPay:
                makeJsonObjReq();
        }
    }
}