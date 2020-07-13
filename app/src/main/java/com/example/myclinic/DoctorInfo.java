package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class DoctorInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE = 123;
    private EditText etqualification, etcertification, etfee, etlocation, etexperience;
    private String doctorID, doctorName, specialization, experience, qualification, certifications, location;
    private String fee;
    private ImageView docPic;
    ProgressDialog progressDialog;
    private Uri imageUri;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        Toast.makeText(this, "Upload your profile pic", Toast.LENGTH_SHORT).show();

        etqualification = findViewById(R.id.etQualifications);
        etcertification = findViewById(R.id.etCertifications);
        etexperience = findViewById(R.id.etExperience);
        etlocation = findViewById(R.id.etLocation);
        etfee = findViewById(R.id.etFees);
        Spinner specialisation = findViewById(R.id.etSpecialist);
        Button submit = findViewById(R.id.btnSubmit);
        docPic = findViewById(R.id.doctorPic);
        progressDialog = new ProgressDialog(this);
        imageUri = null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Specialization,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        specialisation.setAdapter(adapter);
        specialisation.setOnItemSelectedListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                if(validate()){
                    Doctors doctor= new Doctors(doctorID,doctorName,specialization,experience,qualification,certifications,location,fee);
                    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference= firebaseDatabase.getReference("Doctors");
                    databaseReference.child(doctorID).setValue(doctor);

                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    storageReference = firebaseStorage.getReference("Doctors").child(doctorID);
                    UploadTask uploadTask = storageReference.putFile(imageUri);
                    progressDialog.dismiss();
                    Toast.makeText(DoctorInfo.this , "DETAILS UPLOADED SUCCESSFULLY ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DoctorInfo.this, WorkLocation.class));

                } else{
                    progressDialog.dismiss();
                    Toast.makeText(DoctorInfo.this , "DETAILS UPLOADED UNSUCCESSFUL.. PLEASE ENTER ALL DETAILS ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        docPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });

    }

    public boolean validate(){

        boolean flag= false;
        doctorID = getIntent().getStringExtra("DoctorID");
        doctorName = getIntent().getStringExtra("DoctorName");
        experience = etexperience.getText().toString()+" Years";
        certifications = etcertification.getText().toString();
        qualification = etqualification.getText().toString();
        location = etlocation.getText().toString();
        fee = "Rs"+etfee.getText().toString();
        if(experience.isEmpty() && specialization.isEmpty() && imageUri == null && certifications.isEmpty() && qualification.isEmpty() && location.isEmpty()
                && fee.isEmpty()){
            Toast.makeText(this , "please enter all the details",Toast.LENGTH_SHORT).show();
        }else if(imageUri== null) {
            Toast.makeText(this, "please upload your profile pic", Toast.LENGTH_SHORT).show();
        }else {
                flag = true;
            }
        return flag;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;
        if(requestCode == PICK_IMAGE &&  data.getData()!= null){

            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                docPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
