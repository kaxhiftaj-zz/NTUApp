package com.techease.ntuapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by kaxhiftaj on 3/9/17.
 */

public class EmployerAdapter  extends RecyclerView.Adapter<EmployerAdapter.MyViewHolder>  {

    List<Employer> feedHelperList;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Context context ;



    public EmployerAdapter(Context context, List feedHelperList) {
        this.context = context;
        this.feedHelperList = feedHelperList;
    }


    @Override
    public EmployerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Employer feedHelper = feedHelperList.get(position);
        holder.name.setText(feedHelper.getName());
        holder.subject.setText(feedHelper.getSubject());
        holder.bio.setText(String.valueOf(feedHelper.getBio()));
        holder.apply.setText(String.valueOf(feedHelper.getApply()));;
       Picasso.with(this.context).load(feedHelper.getLogo()).into(holder.ivlogo);

    }

    @Override
    public int getItemCount() {
        return feedHelperList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, bio, subject, apply;
        com.makeramen.roundedimageview.RoundedImageView ivlogo ;

        public MyViewHolder(View v) {
            super(v);

            ivlogo = (com.makeramen.roundedimageview.RoundedImageView) v.findViewById(R.id.ivProfilePic);
            name = (TextView) v.findViewById(R.id.name);
            bio = (TextView) v.findViewById(R.id.bio);
            subject = (TextView) v.findViewById(R.id.subject);
            apply = (TextView) v.findViewById(R.id.apply);


        }

    }

}
