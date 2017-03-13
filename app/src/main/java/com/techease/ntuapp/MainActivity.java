package com.techease.ntuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.core.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private Uri picUri;
    Uri downloadUrl = Uri.EMPTY;
    List<Employer> employers = new ArrayList<>();
    EmployerAdapter mEmployerAdapter;
    RecyclerView mRecyclerView;
    android.content.Context context ;
    ProgressDialog progressDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this ;
        sharedPreferences = getSharedPreferences("Reg", 0);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        final ValueEventListener userListener;

        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
       // storageRef = storage.getReferenceFromUrl("gs://ntuapp-20836.appspot.com");

        mRecyclerView = (RecyclerView) findViewById(R.id.feedRecycler);
        LinearLayoutManager layoutm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutm);

//        String name = "kashif";
//        String logo = "kashif";
//        String subject = "kashif";
//        String bio = "kashif";
//        String apply = "kashif";
//
//
//        Employer employer = new Employer(name, logo, bio , subject , apply );
//        mDatabase.child(name).setValue(employer);

        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                employers.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    if (userSnapshot.getValue() != null) {
                        Employer employ = userSnapshot.getValue(Employer.class);
                        employ.setName(userSnapshot.child("name").getValue().toString());
                        Log.d("name", employ.getName());
                        employ.setBio(userSnapshot.child("bio").getValue().toString());
                        Log.d("bio" , employ.getBio());
                        employ.setLogo(userSnapshot.child("logo").getValue().toString());
                        Log.d("logo" , employ.getLogo());
                        employ.setSubject(userSnapshot.child("subject").getValue().toString());
                        Log.d("subject" , employ.getSubject());
                        employ.setApply(userSnapshot.child("apply").getValue().toString());
                        Log.d("apply" , employ.getApply());
                        employers.add(employ);
                        progressDialog.dismiss();
                    }
                }
                mEmployerAdapter = new EmployerAdapter(context,employers);
                mRecyclerView.setAdapter(mEmployerAdapter);
              //  mDatabase.child("users").removeEventListener(postListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(MainActivity.this, String.valueOf(databaseError.toException()), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

   }

}
