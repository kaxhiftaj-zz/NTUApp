package com.techease.ntuapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StudentLoginFragment extends Fragment {
    EditText etSignInEmail, etSigninPass;
    ImageView ivSignIn;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_login, container, false);

        Firebase.setAndroidContext(getActivity());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        etSignInEmail = (EditText) v.findViewById(R.id.etSignInEmail);
        etSigninPass = (EditText) v.findViewById(R.id.etSignInPassword);
        ivSignIn = (ImageView) v.findViewById(R.id.ivSignIn);

        ivSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                String email = etSignInEmail.getText().toString();
                String password = etSigninPass.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "You are now Logged in", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    } else {
                                        progressDialog.dismiss();
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Error")
                                                .setMessage(task.getException().toString())
                                                .setPositiveButton(android.R.string.ok, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                            });


                } else {
                    etSignInEmail.setError("Invalid Email");
                }
            }
        });
        return v;
    }


}
