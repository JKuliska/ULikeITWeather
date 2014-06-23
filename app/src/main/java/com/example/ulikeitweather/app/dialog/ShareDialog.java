package com.example.ulikeitweather.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.ULikeITWeatherConfig;
import com.example.ulikeitweather.app.activity.MainActivity;

/**
 * shows an options dialog for sharing the weather information
 */

public class ShareDialog extends DialogFragment {

    private static final String SHARE_MESSAGE_BUNDLE_KEY = "message";
    private String mMessage;

    public static ShareDialog newInstance(String message) {
        ShareDialog shareDialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putString(SHARE_MESSAGE_BUNDLE_KEY, message);
        shareDialog.setArguments(args);
        return shareDialog;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            handleArguments(args);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setTitle(R.string.dialog_share_title);
        mDialogBuilder.setItems(R.array.view_list_share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent;
                switch (which) {
                    default: //share
                        sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, mMessage);
                        sendIntent.setType("text/plain");
                        break;
                    case 1: //share via email
                        Uri uri = Uri.parse("mailto:");
                        sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                        sendIntent.putExtra("body", mMessage);
                        break;
                    case 2: //share via message
                        uri = Uri.parse("smsto:");
                        sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                        sendIntent.putExtra("sms_body", mMessage);
                        break;
                }
                startActivity(sendIntent);
            }
        });

        return mDialogBuilder.create();
    }

    private void handleArguments(Bundle args) {
        if(args.containsKey(SHARE_MESSAGE_BUNDLE_KEY)) {
            mMessage = args.getString(SHARE_MESSAGE_BUNDLE_KEY);
        }
    }
}
