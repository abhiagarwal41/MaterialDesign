package com.abhishek.materialdesign;

import android.app.IntentService;
import android.content.Intent;


public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        HomeActivity.showSnackbar("Service started");
    }

}
