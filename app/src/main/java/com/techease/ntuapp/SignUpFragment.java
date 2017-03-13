package com.techease.ntuapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpFragment extends Fragment {

    EditText etSignupName, etSignupEmail, etSignupPassword, etSIgnupAge;
    TextView tvLogin ;
    ImageView ivSiginUp;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        Firebase.setAndroidContext(getActivity());
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);


        etSignupName = (EditText) v.findViewById(R.id.etSignupName);
        etSignupEmail = (EditText) v.findViewById(R.id.etSignupEmail);
        etSignupPassword = (EditText) v.findViewById(R.id.etSignuppassword);
        etSIgnupAge = (EditText) v.findViewById(R.id.etSignupAge);
        ivSiginUp = (ImageView) v.findViewById(R.id.ivSignUp);
        tvLogin = (TextView)v.findViewById(R.id.tvlogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new StudentLoginFragment();
                getFragmentManager().beginTransaction().
                        replace(R.id.fragmentContainer, fragment).commit();
            }
        });
        ivSiginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String name = etSignupName.getText().toString();
                final String email = etSignupEmail.getText().toString();
                final String age = etSIgnupAge.getText().toString();
                final String password = etSignupPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),  "good", Toast.LENGTH_SHORT).show();

                            editor.putString("UserID", task.getResult().getUser().getUid());
                            editor.putString("Provider", "Email");
                            editor.putBoolean("Login", true);
                            editor.commit();

                            getActivity().finish();
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
            }
        });
        return v ;
    }

}
