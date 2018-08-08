package com.kashifirshad.consultant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PreferencesActivity extends AppCompatActivity {

    CheckBox checkBoxUnReadOnly;
    EditText editTextsyncDuration;
    EditText editTextFirstName;
    EditText editTextMiddleName;
    EditText editTextLastName;
    EditText editTextSkypeId;
    EditText editTextWatsAppNo;
    EditText editTextAddressLine1;
    EditText editTextAddressLine2;
    EditText editTextCity;
    EditText editTextState;
    EditText editTextCountry;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        user = MainActivity.user;

        checkBoxUnReadOnly = (CheckBox) findViewById(R.id.checkBoxUnReadOnly);
       editTextsyncDuration = (EditText) findViewById(R.id.editTextSyncDuration);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextMiddleName = (EditText) findViewById(R.id.editTextMiddleName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextSkypeId = (EditText) findViewById(R.id.editTextSkypeId);
       editTextWatsAppNo = (EditText) findViewById(R.id.editTextWatsAppNo);
        editTextAddressLine1 = (EditText) findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = (EditText) findViewById(R.id.editTextAddressLine2);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextState = (EditText) findViewById(R.id.editTextState);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);

        checkBoxUnReadOnly.setChecked(user.getShowUnreadStoriesOnly() == 1);
        editTextsyncDuration.setText(Integer.toString(user.getSyncDuration()) );
        editTextFirstName.setText(user.getFirstName());
        editTextMiddleName.setText(user.getMiddleName());
        editTextLastName.setText(user.getLastName());
        editTextSkypeId.setText(user.getSkypeId());
        editTextWatsAppNo.setText(user.getWatsAppNo());
        editTextAddressLine1.setText(user.getAddressLine1());
        editTextAddressLine2.setText(user.getAddressLine2());
        editTextCity.setText(user.getCity());
        editTextState.setText(user.getState());
        editTextCountry.setText(user.getCountry());


        final Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkBoxUnReadOnly.isChecked()){
                    user.setShowUnreadStoriesOnly(1);
                }else{
                    user.setShowUnreadStoriesOnly(0);
                }
                user.setSyncDuration(Integer.parseInt(editTextsyncDuration.getText().toString()));
                user.setFirstName(editTextFirstName.getText().toString());
                user.setLastName(editTextLastName.getText().toString());
                user.setMiddleName(editTextMiddleName.getText().toString());
                user.setSkypeId(editTextSkypeId.getText().toString());
                user.setWatsAppNo(editTextWatsAppNo.getText().toString());
                user.setAddressLine1(editTextAddressLine1.getText().toString());
                user.setAddressLine2(editTextAddressLine2.getText().toString());
                user.setCity(editTextCity.getText().toString());
                user.setState(editTextState.getText().toString());
                user.setCountry(editTextCountry.getText().toString());
                DatabaseHelper dh = new DatabaseHelper(PreferencesActivity.this);
                dh.updateUser(user);
                dh.closeDB();
                Toast.makeText(PreferencesActivity.this,"Preferences Saved", Toast.LENGTH_LONG).show();
                MainActivity.user = user;

                int delayMS = user.getSyncDuration()*1000*60;
                MainActivity.syncTimer.cancel();
                MainActivity.syncTimer.purge();
                MainActivity.syncTimer = new Timer();
                MainActivity.syncTimer.scheduleAtFixedRate(
                        new TimerTask()
                        {
                            public void run()
                            {
                                startService(new Intent(PreferencesActivity.this, SyncService.class));
                            }
                        },
                        0,      // run first occurrence immediately
                        delayMS);  // run every three seconds

                btnHome.performClick();
                btnHome.setPressed(true);
                startService(new Intent(PreferencesActivity.this, SyncService.class));

            }});
    }

    @Override
    public void onBackPressed() {
        final Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.performClick();
        btnHome.setPressed(true);
    }
}
