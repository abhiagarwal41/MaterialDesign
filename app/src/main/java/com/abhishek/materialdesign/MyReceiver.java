package com.abhishek.materialdesign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(Constants.TAG, "I have no idea what the headset state is");

        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d(Constants.TAG, "Headset is unplugged");
                    break;
                case 1:
                    Log.d(Constants.TAG, "Headset is plugged");
                    break;
                default:
                    Log.d(Constants.TAG, "I have no idea what the headset state is");
            }
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
