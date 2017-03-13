package com.techease.ntuapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class EmployersFragment extends Fragment {

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
    ProgressDialog progressDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_employers, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        final ValueEventListener userListener;
        Firebase.setAndroidContext(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        // storageRef = storage.getReferenceFromUrl("gs://ntuapp-20836.appspot.com");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.feedRecycler);
        LinearLayoutManager layoutm = new LinearLayoutManager(getActivity());
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
                mEmployerAdapter = new EmployerAdapter(getActivity(),employers);
                mRecyclerView.setAdapter(mEmployerAdapter);
                //  mDatabase.child("users").removeEventListener(postListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getActivity(), String.valueOf(databaseError.toException()), Toast.LENGTH_SHORT).show();
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

        return  v ;
    }

}
