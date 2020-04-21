package com.example.socialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    String uploadId,userId;
    private DatabaseReference mDatabaseRef, myRef;
    public TextView textViewName, postUser,likeText;
    public ImageView imageView, profileImage, likeImage;
    CardView cardView;
    LinearLayout linearLayout;
    private RecyclerView mRecyclerView;
//    private List<Upload> mComments;
    private List<Upload> mComments;
    private CommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        textViewName = findViewById(R.id.text_view_name);
        imageView = findViewById(R.id.image_view_upload);
        postUser = findViewById(R.id.postUsername);
        profileImage = findViewById(R.id.profileImage);
        cardView = findViewById(R.id.CardView);
        linearLayout=findViewById(R.id.linearLayout);
        mComments = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        uploadId = bundle.getString("uploadId");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(uploadId);

        mDatabaseRef.child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Upload upload = postSnapshot.getValue(Upload.class);
                    mComments.add(upload);
                    mAdapter = new CommentAdapter(ImageActivity.this, mComments);
                    mRecyclerView.setAdapter(mAdapter);
                }
                Toast.makeText(ImageActivity.this, ""+ mComments, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userId=dataSnapshot.child("userId").getValue().toString();
                Picasso.get()
                        .load(dataSnapshot.child("imageUrl").getValue().toString())
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .fit()
                        .centerInside()
                        .into(imageView);
                textViewName.setText(dataSnapshot.child("name").getValue().toString());

                myRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Picasso.get()
                                .load(dataSnapshot.child("profileImageUrl").getValue().toString())
                                .placeholder(R.drawable.ic_image_black_24dp)
                                .fit()
                                .centerCrop()
                                .into(profileImage);
                        String User = dataSnapshot.child("username").getValue().toString();
                        postUser.setText(User);

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





        //.....................Bottom Navigation...........................................
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.about:
                        startActivity(new Intent(ImageActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Dashboard:
                        startActivity(new Intent(ImageActivity.this, SearchActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;

                }
                return false;
            }
        });
        //.....................Bottom Navigation...........................................
    }
}
