package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView profileName, changePassword, profileAge, profileGender,profileEmail, profilePhone;
    private Button edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private static final String TAG = "UpdateProfile";


    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private EditText newName, newAge, newDOB, newPhone, newGender, newEmail;
    private Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        //profilePic = findViewById(R.id.ivProfilePic);
        profileName = findViewById(R.id.tvProfileName);
        profileAge = findViewById(R.id.tvProfileAge);
        profileGender = findViewById(R.id.tvProfileGender);
        profileEmail = findViewById(R.id.tvProfileEmail);
        profilePhone = findViewById(R.id.tvProfilePhone);
        edit = findViewById(R.id.btnEdit);
        changePassword = findViewById(R.id.tvProfilePassword);

        newName = findViewById(R.id.etRName);
        newAge = findViewById(R.id.etAge);
        newDOB = findViewById(R.id.etDOB);
        newGender = findViewById(R.id.etGender);
        newPhone = findViewById(R.id.etPhoneNo);
        update = findViewById(R.id.btnUpdate);
        newEmail = findViewById(R.id.etEmail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("DoctorProfile").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DoctorProfile doctorProfile = dataSnapshot.getValue(DoctorProfile.class);
                profileName.setText("Name: "+doctorProfile.getName());
                profileAge.setText("Age: "+doctorProfile.getAge());
                profileGender.setText("Gender: "+doctorProfile.getGender());
                profileEmail.setText("Email: "+doctorProfile.getEmail());
                profilePhone.setText("Phone No: "+doctorProfile.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorDetailsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileName.setVisibility(View.GONE);
                profileAge.setVisibility(View.GONE);
                profileGender.setVisibility(View.GONE);
                profileEmail.setVisibility(View.GONE);
                profilePhone.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);

                newName.setVisibility(View.VISIBLE);
                newAge.setVisibility(View.VISIBLE);
                newDOB.setVisibility(View.VISIBLE);
                newGender.setVisibility(View.VISIBLE);
                newPhone.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
                final DatabaseReference databaseReference = firebaseDatabase.getReference().child("DoctorProfile").child(firebaseAuth.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DoctorProfile userProfile = dataSnapshot.getValue(DoctorProfile.class);
                        newName.setText(userProfile.getName());
                        newAge.setText(userProfile.getAge());
                        newDOB.setText(userProfile.getDob());
                        newGender.setText(userProfile.getGender());
                        newEmail.setText(userProfile.getEmail());
                        newPhone.setText(userProfile.getPhone());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(DoctorDetailsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });

                newDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                DoctorDetailsActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                onDateSetListener,
                                year ,month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }
                });

                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        Log.d(TAG,"onDateSet: date: "+ dayOfMonth +"/"+ month +"/"+ year);

                        String date= dayOfMonth+ "/"+ month+"/"+ year;
                        newDOB.setText(date);
                    }
                };

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = newName.getText().toString();
                        String age = newAge.getText().toString();
                        String gender = newGender.getText().toString();
                        String dob = newDOB.getText().toString();
                        String email = newEmail.getText().toString();
                        String phone = newPhone.getText().toString();

                        DoctorProfile userProfile = new DoctorProfile(name,age,email,gender,phone,dob);
                        databaseReference.setValue(userProfile);
                        finish();
                    }
                });
                     }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorDetailsActivity.this, UpdatePasswordActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
