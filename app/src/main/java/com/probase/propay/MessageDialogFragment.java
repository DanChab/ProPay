package com.probase.propay;

/**
 * Created by D4n on 6/8/2017.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private String amount;
    private String merchantId;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, String amount, String merchantId, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.amount = amount;
        fragment.merchantId = merchantId;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
//                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                    //Get params from the bundle
                    Log.d("QRDATA", amount);
                    Log.d("QRDATA", merchantId);
                    // Pass params to PaymentActivity
                    passParamsToActivity(amount, merchantId);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }
            }
        });

        return builder.create();
    }

    public void passParamsToActivity(String amount, String merchantId){
        Intent intent = new Intent(getActivity(),PaymentActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("merchantId",merchantId);
        startActivity(intent);
    }
}
