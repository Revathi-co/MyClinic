package com.example.myclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manageConnections();

        Toast.makeText(HomeActivity.this, "Please, click on work places for work location", Toast.LENGTH_SHORT).show();
        Button logout = findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });

        TextView workPlace = findViewById(R.id.tvWorkLoc);
        workPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, WorkLocation.class));
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(HomeActivity.this,HomeActivity.class));
                        break;
                    case R.id.nav_details:
                        startActivity(new Intent(HomeActivity.this,DoctorDetailsActivity.class));
                        break;
                    case R.id.nav_UploadPR:
                        startActivity(new Intent(HomeActivity.this,UpdateDoctorInfo.class));
                        break;
                    case R.id.nav_notify:
                        startActivity(new Intent(HomeActivity.this,NotificationActivity.class));
                        break;
                    case R.id.nav_Logout:
                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        String current_id = FirebaseAuth.getInstance().getUid();

                        Map<String, Object> tokenRemove = new HashMap<>();
                        tokenRemove.put("token_id", FieldValue.delete());
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        assert current_id != null;
                        firebaseFirestore.collection("Doctors").document(current_id).set(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                firebaseAuth.signOut();
                                finish();
                               startActivity(new Intent(HomeActivity.this, MainActivity.class));

                            }
                        });
                        break;
                }

                drawer.closeDrawers();

                return false;
            }
        });


    }

    private void manageConnections() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors")
                .child(FirebaseAuth.getInstance().getUid());
        final DatabaseReference connection = FirebaseDatabase.getInstance().getReference(".info/connected");
        connection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag = dataSnapshot.getValue(Boolean.class);
                if(flag){
                    DatabaseReference ref = reference.child("status");
                    ref.setValue("online");
                    ref.onDisconnect().setValue("offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
