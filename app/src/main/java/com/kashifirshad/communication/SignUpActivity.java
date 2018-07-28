package com.kashifirshad.communication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.kashifirshad.communication.ServerRequest.ct;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button allowPermission = (Button) findViewById(R.id.btnSignUp);
        allowPermission.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                EditText editEmail = (EditText) findViewById(R.id.email);
                String email = editEmail.getText().toString();

                EditText editPass1 = (EditText) findViewById(R.id.pass1);
                String pass1 = editPass1.getText().toString();

                EditText editPass2 = (EditText) findViewById(R.id.pass2);
                String pass2 = editPass2.getText().toString();

                if(!Common.isValidEmail(editEmail.getText())){
                    Toast.makeText(getApplicationContext(), "Invalid Email Address.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(!pass1.equals(pass2) || pass1.isEmpty() || pass2.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password and Re-Type Password not matched.",
                            Toast.LENGTH_LONG).show();
                    return;
                }


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = "http://www.kashifirshad.com/AndroidProjects/UserSignUp.php";
                
                final User usr = new User();
                usr.setEmailAddress(email);
                usr.setPassword(Common.get_SHA_512_SecurePassword(pass1));
                usr.setCreatedAt(Common.getDateTime());
                usr.setUpdatedAt(Common.getDateTime());

                Gson gson = new Gson();
                String json = gson.toJson(usr);
                JSONObject request = new JSONObject();
                try {
                    request = new JSONObject(json);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response***",response.toString());
                        try {
                            JSONObject resultsOBJ = response.getJSONObject("results");
                            String msg = resultsOBJ.getString("msg");
                            if(msg.equals("Success")){
                                Long UserId = resultsOBJ.getLong("Id");
                                usr.setId(UserId);
                                usr.setServerId(UserId);
                                DatabaseHelper dh = new DatabaseHelper(getApplicationContext());
                                dh.createUser(usr);
                                Toast.makeText(getApplicationContext(), "User Created, Please use email Token, just sent to you, to verify email at login screen!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("email", usr.getEmailAddress());
                                bundle.putBoolean("isFirstTime", true);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                            } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
                queue.add(jsObjRequest);
    }});
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
