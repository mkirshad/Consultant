package com.kashifirshad.communication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottonPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotton_password);

        final EditText etToken = (EditText) findViewById(R.id.token);
        final EditText etEmail = (EditText) findViewById(R.id.email);
        final EditText etPass = (EditText) findViewById(R.id.pass1);
        final EditText etPass2 = (EditText) findViewById(R.id.pass2);

        Button btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String pass2 = etPass2.getText().toString();
                String strToken = etToken.getText().toString();
                int token = 0;
                if(strToken != null && !strToken.isEmpty())
                    token = Integer.parseInt(strToken);
                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email is empty!", Toast.LENGTH_LONG).show();
                    return;
                }else if(etPass.getVisibility() == View.VISIBLE && pass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password is empty!", Toast.LENGTH_LONG).show();
                    return;
                }else if(etPass.getVisibility() == View.VISIBLE && !pass.equals(pass2) ){
                    Toast.makeText(getApplicationContext(), "Password and Re-Type Password not matched!", Toast.LENGTH_LONG).show();
                    return;
                }else if(etToken.getVisibility() == View.VISIBLE && token == 0){
                        Toast.makeText(getApplicationContext(), "Email Token is empty!", Toast.LENGTH_LONG).show();
                        return;
                }

                DatabaseHelper dh = new DatabaseHelper(getApplicationContext());
                final  User usr = dh.getUser(email);
                usr.setEmailAddress(email);
                usr.setPassword(Common.get_SHA_512_SecurePassword(pass));
                usr.setToken(token);

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://www.kashifirshad.com/AndroidProjects/ForgottonPassword.php";

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
                                DatabaseHelper dh = new DatabaseHelper(getApplicationContext());
                                if(usr.getId() == 0){
                                    usr.setId(UserId);
                                    usr.setServerId(UserId);
                                    usr.setIsLoggedIn(1);
                                    dh.createUser(usr);
                                }else{
                                    usr.setServerId(UserId);
                                    usr.setIsLoggedIn(1);
                                    dh.updateUser(usr);
                                }
                                Toast.makeText(getApplicationContext(), "Successfully Logged In!", Toast.LENGTH_LONG).show();
                                MainActivity.user = usr;
                                if(usr.getEmailAddress().equals("kashif.ir@gmail.com"))
                                    MainActivity.isSuperUser = true;
                                // Do after Login Activity
                                Intent intent = new Intent(ForgottonPasswordActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                if(msg.contains("Token")){
                                    etToken.setVisibility(View.VISIBLE);
                                    etPass.setVisibility(View.VISIBLE);
                                    etPass2.setVisibility(View.VISIBLE);
                                }
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
