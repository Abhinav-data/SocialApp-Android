package com.example.socialapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("uploads");


    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {

        final Upload uploadCurrent = mUploads.get(getItemCount() - position - 1);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uploadCurrent.getUserId());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String User = dataSnapshot.child("username").getValue().toString();
                String image = "" + dataSnapshot.child("profileImageUrl").getValue();
                holder.textViewName.setText(uploadCurrent.getName());
                holder.postUser.setText(User);
                Picasso.get()
                        .load(uploadCurrent.getImageUrl())
                        .fit()
                        .centerInside()
                        .into(holder.imageView);
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.ic_account)
                        .fit()
                        .centerCrop()
                        .into(holder.profileImage);

                myRef.child(uploadCurrent.getUploadId()).child("Likes").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DrawableCompat.setTint(holder.likeImage.getDrawable(), ContextCompat.getColor(mContext, R.color.colorAccent));
                        } else {
                            DrawableCompat.setTint(holder.likeImage.getDrawable(), ContextCompat.getColor(mContext, R.color.colorPrimary));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("userId", uploadCurrent.getUserId());
                mContext.startActivity(intent);
            }
        });

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.likeImage.getTag().equals("UnLiked")) {
                    Map newMap = new HashMap();
                    newMap.put(user.getUid(), user.getUid());
                    myRef.child(uploadCurrent.getUploadId()).child("Likes").updateChildren(newMap);
                    holder.likeImage.setTag("Liked");
                    DrawableCompat.setTint(holder.likeImage.getDrawable(), ContextCompat.getColor(mContext, R.color.colorAccent));
                } else {
                    myRef.child(uploadCurrent.getUploadId()).child("Likes").child(user.getUid()).removeValue();
                    holder.likeImage.setTag("UnLiked");
                    DrawableCompat.setTint(holder.likeImage.getDrawable(), ContextCompat.getColor(mContext, R.color.colorPrimary));
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, postUser;
        public ImageView imageView, profileImage, likeImage;
        CardView cardView;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            postUser = itemView.findViewById(R.id.postUsername);
            profileImage = itemView.findViewById(R.id.profileImage);
            cardView = itemView.findViewById(R.id.CardView);
            likeImage = itemView.findViewById(R.id.like);
        }


    }

}
