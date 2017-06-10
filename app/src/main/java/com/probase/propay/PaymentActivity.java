package com.probase.propay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText etPin;
    private Button btPay;
    private String amount;
    private String merchantId;
    private ProgressDialog pDialog;
    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req";
    private String  mPin;

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
            Toast toast = Toast.makeText(getApplicationContext(), amount + "AM HERE" + merchantId, Toast.LENGTH_SHORT);
            toast.show();

        }

        etPin = (EditText) findViewById(R.id.etPin);
        mPin = etPin.getText().toString();
        btPay = (Button) findViewById(R.id.btPay);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btPay.setOnClickListener(this);
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
        Map<String, String> params = new HashMap<String, String>();
        Log.d("AMOUNT", amount);
        Log.d("AMOUNT", mPin);
        Log.d("AMOUNT", merchantId);
        params.put("pin",mPin);
        params.put("amount", amount);
        params.put("merchantId", merchantId);
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_JSON_OBJECT, new JSONObject(params) ,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("AMOUNT", amount);
                Log.d("AMOUNT", mPin);
                Log.d("AMOUNT", merchantId);
                params.put("pin",mPin);
                params.put("amount", amount);
                params.put("merchantId", merchantId);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btPay:
                makeJsonObjReq();
        }
    }
}