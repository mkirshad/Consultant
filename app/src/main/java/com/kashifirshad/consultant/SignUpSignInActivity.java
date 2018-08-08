package com.kashifirshad.consultant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpSignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_sign_in);

        Button allowPermission = (Button) findViewById(R.id.btnSignUp);
        allowPermission.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSignInActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnForgotten = (Button) findViewById(R.id.btnForgotten);
        btnForgotten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSignInActivity.this, ForgottonPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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
