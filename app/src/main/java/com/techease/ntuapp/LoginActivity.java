package com.techease.ntuapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etSignInEmail, etSigninPass;
    ImageView ivSignIn;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLogin;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        sharedPreferences = getSharedPreferences("Reg", 0);
        editor = sharedPreferences.edit();
        isLogin = sharedPreferences.getBoolean("Login", false);
        if (isLogin) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        etSignInEmail = (EditText) findViewById(R.id.etSignInEmail);
        etSigninPass = (EditText) findViewById(R.id.etSignInPassword);
        ivSignIn = (ImageView) findViewById(R.id.ivSignIn);

        ivSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivSignIn:
                progressDialog.show();
                String email = etSignInEmail.getText().toString();
                String password = etSigninPass.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        editor.putString("UserID", task.getResult().getUser().getUid());
                                        editor.putString("Provider", "Email");
                                        editor.putBoolean("Login", true);
                                        editor.commit();
                                        LoginActivity.this.finish();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        progressDialog.dismiss();
                                        new AlertDialog.Builder(LoginActivity.this)
                                                .setTitle("Error")
                                                .setMessage(task.getException().toString())
                                                .setPositiveButton(android.R.string.ok, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                            });

                    break;

                } else {
                    etSignInEmail.setError("Invalid Email");
                }
        }

    }


}
