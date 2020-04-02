package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    boolean f = true;


    SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseReference mDatabaseRef, myRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            setContentView(R.layout.activity_main);
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mUploads = new ArrayList<>();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
            myRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Friends");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    if (f) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final Upload upload = postSnapshot.getValue(Upload.class);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ds) {
                                    if (ds.hasChild(upload.getUserId()) || user.getUid().equals(upload.getUserId())) {
                                        mUploads.add(upload);
                                        mAdapter = new ImageAdapter(MainActivity.this, mUploads);
                                        mRecyclerView.setAdapter(mAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            f = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            swipeRefreshLayout = findViewById(R.id.refreshLayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            });

            FloatingActionButton floatingActionButton = findViewById(R.id.floating_action);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent uploadPostIntent = new Intent(MainActivity.this, uploadPost.class);
                    startActivity(uploadPostIntent);
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
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.Dashboard:
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
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
}
