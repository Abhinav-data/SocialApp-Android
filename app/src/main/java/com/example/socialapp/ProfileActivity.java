package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    EditText statusTV;
    Button submit,logout,uploadPost;
    TextView yStatus,uName;
    boolean f=true;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Users").child(user.getUid());




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Reached Profile","Success");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        logout=findViewById(R.id.logOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent=new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        uploadPost=findViewById(R.id.uploadPost);
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadPostIntent=new Intent(ProfileActivity.this,uploadPost.class);
                startActivity(uploadPostIntent);
            }
        });


        retrieveDataFromDB();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataInDatabase();
            }
        });
        //..........................Profile Stuff......................................................................

    }

    private void retrieveDataFromDB() {
        yStatus=findViewById(R.id.yourStatus);
        statusTV = (EditText) findViewById(R.id.statusValue);

        uName=findViewById(R.id.uName);
        submit = (Button) findViewById(R.id.btnSubmit);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String status = dataSnapshot.child("status").getValue(String.class);
                    String uName1 = dataSnapshot.child("username").getValue(String.class);
                    yStatus.setText(status);
                    uName.setText(uName1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Unable to fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addDataInDatabase() {
        final Map newMap= new HashMap();
        newMap.put("status",statusTV.getText().toString().trim());
        myRef.updateChildren(newMap);
        Toast.makeText(ProfileActivity.this, "Saved In DB", Toast.LENGTH_SHORT).show();
    }

}