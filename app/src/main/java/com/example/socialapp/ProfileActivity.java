package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProfileImageAdapter mAdapter;
    boolean f=true;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    EditText statusTV;
    Button submit, logout, uploadPost;
    TextView yStatus, uName;
    ImageView profileImage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Users").child(user.getUid());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Reached Profile", "Success");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profileImage);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (f) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        if(upload.getUserId().equals(user.getUid())) {
                            mUploads.add(upload);
                        }

                    }
                    mAdapter = new ProfileImageAdapter(ProfileActivity.this, mUploads);
                    mRecyclerView.setAdapter(mAdapter);
                    f=false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String image = "" + dataSnapshot.child("profileImageUrl").getValue();
            Picasso.get().load(image).placeholder(R.drawable.ic_account).fit().centerCrop().into(profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton floatingActionButton=findViewById(R.id.floating_action);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent=new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(loginIntent);

            }
        });



        //.....................Bottom Navigation...........................................

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.about);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.about:
                        return true;
                    case R.id.Dashboard:
                        startActivity(new Intent(ProfileActivity.this,SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        //.....................Bottom Navigation...........................................
        //..........................Profile Stuff......................................................................

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileImageIntent=new Intent(ProfileActivity.this,ProfileImageActivity.class);
                startActivity(profileImageIntent);
            }
        });


        retrieveDataFromDB();
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addDataInDatabase();
//            }
//        });
        //..........................Profile Stuff......................................................................

    }

    private void retrieveDataFromDB() {
//        yStatus=findViewById(R.id.yourStatus);
//        statusTV = (EditText) findViewById(R.id.statusValue);

        uName=findViewById(R.id.uName);
//        submit = (Button) findViewById(R.id.btnSubmit);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String status = dataSnapshot.child("status").getValue(String.class);
                    String uName1 = dataSnapshot.child("username").getValue(String.class);
//                    yStatus.setText(status);
                    uName.setText(uName1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Unable to fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void addDataInDatabase() {
//        final Map newMap = new HashMap();
//        newMap.put("status", statusTV.getText().toString().trim());
//        myRef.updateChildren(newMap);
//        Toast.makeText(ProfileActivity.this, "Saved In DB", Toast.LENGTH_SHORT).show();
//    }

}