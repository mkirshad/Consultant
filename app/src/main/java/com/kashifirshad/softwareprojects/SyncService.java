package com.kashifirshad.softwareprojects;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by biome on 3/29/2018.
 */

    public class SyncService extends IntentService {
        @Override
        protected void onHandleIntent(Intent workIntent) {
            // Gets data from the incoming Intent
            String dataString = workIntent.getDataString();
            // Do work here, based on the contents of dataString
//            Toast.makeText(SyncService.this,"Service Is Running!", Toast.LENGTH_LONG).show();
            Log.e("service***","Service Runnimg");
            if(MainActivity.user != null && MainActivity.user.getEmailAddress() != null){
                ServerRequest sr = new ServerRequest(SyncService.this);
            }
        }

        public SyncService(){
            super("SyncService");
        }
    }
