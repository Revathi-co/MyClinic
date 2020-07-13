package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDoctorInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView  tvSpecialisation, tvqualification, tvcertification, tvfee, tvlocation, tvexperience;
    private EditText etqualification, etcertification, etfee, etlocation, etexperience;
    private Spinner specialisation;
    private String specialization, experience, qualification, certifications, location;
    private String fee;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_info);
        tvqualification = findViewById(R.id.tvQualifications);
        tvcertification = findViewById(R.id.tvCertifications);
        tvexperience = findViewById(R.id.tvExperience);
        tvlocation = findViewById(R.id.tvLocation);
        tvfee = findViewById(R.id.tvFees);
        tvSpecialisation = findViewById(R.id.tvSpecialist);
        final Button edit = findViewById(R.id.btnEdit);

        etqualification = findViewById(R.id.etQualifications);
        etcertification = findViewById(R.id.etCertifications);
        etexperience = findViewById(R.id.etExperience);
        etlocation = findViewById(R.id.etLocation);
        etfee = findViewById(R.id.etFees);
        specialisation = findViewById(R.id.etSpecialist);
        final Button Update = findViewById(R.id.btnUpdate);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Doctors").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctors doctor = dataSnapshot.getValue(Doctors.class);
                tvSpecialisation.setText("Specialisation: "+doctor.getSpecialization());
                tvqualification.setText("Qualification: "+doctor.getQualification());
                tvexperience.setText("Experience: "+doctor.getExperience());
                tvcertification.setText("Certification: "+doctor.getCertifications());
                tvlocation.setText("Location: "+doctor.getLocation());
                tvfee.setText("Fee: "+doctor.getFee());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateDoctorInfo.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvSpecialisation.setVisibility(View.GONE);
                tvqualification.setVisibility(View.GONE);
                tvexperience.setVisibility(View.GONE);
                tvcertification.setVisibility(View.GONE);
                tvlocation.setVisibility(View.GONE);
                tvfee.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);

                specialisation.setVisibility(View.VISIBLE);
                etqualification.setVisibility(View.VISIBLE);
                etexperience.setVisibility(View.VISIBLE);
                etcertification.setVisibility(View.VISIBLE);
                etlocation.setVisibility(View.VISIBLE);
                etfee.setVisibility(View.VISIBLE);
                Update.setVisibility(View.VISIBLE);

                final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateDoctorInfo.this, R.array.Specialization,
                        R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                specialisation.setAdapter(adapter);
                specialisation.setOnItemSelectedListener(UpdateDoctorInfo.this);

                final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Doctors").child(firebaseAuth.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Doctors doctor = dataSnapshot.getValue(Doctors.class);
                        assert doctor != null;
                        etqualification.setText(doctor.getQualification());
                        etexperience.setText(doctor.getExperience());
                        etcertification.setText(doctor.getCertifications());
                        etlocation.setText(doctor.getLocation());
                        etfee.setText(doctor.getFee());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UpdateDoctorInfo.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });

                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.setMessage("Processing...");
                        progressDialog.show();
                        if(validate()){
                            Doctors doctor= new Doctors(specialization,experience,qualification,certifications,location,fee);
                            FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference= firebaseDatabase.getReference("Doctors");
                            databaseReference.child(firebaseAuth.getUid()).setValue(doctor);
                            progressDialog.dismiss();
                            Toast.makeText(UpdateDoctorInfo.this , "DETAILS UPLOADED SUCCESSFULLY ",Toast.LENGTH_SHORT).show();
                            finish();

                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(UpdateDoctorInfo.this , "DETAILS UPLOADED UNSUCCESSFUL.. PLEASE ENTER ALL DETAILS ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public boolean validate(){

        boolean flag= false;
        experience = etexperience.getText().toString();
        certifications = etcertification.getText().toString();
        qualification = etqualification.getText().toString();
        location = etlocation.getText().toString();
        fee = "Rs "+etfee.getText().toString();
        if(experience.isEmpty() && specialization.isEmpty() && certifications.isEmpty() && qualification.isEmpty() && location.isEmpty()
                && fee.isEmpty()){
            Toast.makeText(this , "please enter all the details",Toast.LENGTH_SHORT).show();
        }else{
            flag = true;
        }
        return flag;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        specialization = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
