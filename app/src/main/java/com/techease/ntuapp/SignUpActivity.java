package com.techease.ntuapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A login screen that offers login via email/password.
 */

public class SignUpActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    EditText etSignupName, etSignupEmail, etSignupPassword, etSIgnupAge;
    TextView tvLogin ;
    ImageView ivSiginUp;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);


        sharedPreferences = getSharedPreferences("Reg", 0);
        editor = sharedPreferences.edit();
        isLogin = sharedPreferences.getBoolean("Login", false);
        if (isLogin) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            SignUpActivity.this.finish();
        }


        etSignupName = (EditText) findViewById(R.id.etSignupName);
        etSignupEmail = (EditText) findViewById(R.id.etSignupEmail);
        etSignupPassword = (EditText) findViewById(R.id.etSignuppassword);
        etSIgnupAge = (EditText) findViewById(R.id.etSignupAge);
        ivSiginUp = (ImageView) findViewById(R.id.ivSignUp);
        tvLogin = (TextView)findViewById(R.id.tvlogin);
        tvLogin.setOnClickListener(this);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this , LoginActivity.class));
            }
        });
        ivSiginUp.setOnClickListener(this);

    }

    @Override
    public void onClick(android.view.View view) {

        switch (view.getId()) {
            case R.id.ivSignUp:
                progressDialog.show();
                final String name = etSignupName.getText().toString();
                final String email = etSignupEmail.getText().toString();
                final String age = etSIgnupAge.getText().toString();
                final String password = etSignupPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,  "good", Toast.LENGTH_SHORT).show();

                            editor.putString("UserID", task.getResult().getUser().getUid());
                            editor.putString("Provider", "Email");
                            editor.putBoolean("Login", true);
                            editor.commit();

                            SignUpActivity.this.finish();
                            progressDialog.dismiss();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            Log.d("MyTag", "Done");
                        } else {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle("Error")
                                    .setMessage(task.getException().toString())
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    }
                });


        }

    }

}


