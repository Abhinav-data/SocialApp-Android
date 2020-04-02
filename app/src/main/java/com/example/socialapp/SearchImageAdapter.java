package com.example.socialapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchImageAdapter extends RecyclerView.Adapter<SearchImageAdapter.SearchViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Users").child(user.getUid());

    public SearchImageAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_image_item, parent, false);
        return new SearchImageAdapter.SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        final Upload uploadCurrent = mUploads.get(getItemCount()-position-1);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ProfileActivity.class);
                intent.putExtra("userId",uploadCurrent.getUserId());
                mContext.startActivity(intent);
            }
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(uploadCurrent.getUserId());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image = "" + dataSnapshot.child("profileImageUrl").getValue();
                String User=dataSnapshot.child("username").getValue().toString();
                holder.postUser.setText(User);
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.ic_account)
                        .fit()
                        .centerCrop()
                        .into(holder.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView postUser;
        ImageView profileImage;
        LinearLayout linearLayout;

        public SearchViewHolder(View itemView){
            super(itemView);
            postUser=itemView.findViewById(R.id.postUsername);
            profileImage=itemView.findViewById(R.id.profileImage);
            linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }
}
