package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class LocationInfo extends AppCompatActivity {

    private EditText hosName, address,city, timeF,timeT;
    String hospitalName;
    String hosAddress;
    String hosCity ;
    String hosTime;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        TextView locationNo = findViewById(R.id.tvlocationNo);
        hosName = findViewById(R.id.etHosName);
        address = findViewById(R.id.etAddress);
        city = findViewById(R.id.etCity);
        timeF = findViewById(R.id.etTimeFrom);
        timeT = findViewById(R.id.etTimeTo);
        Button save = findViewById(R.id.btnLocSave);

        final String loc= getIntent().getStringExtra("location");
        Toast.makeText(this, "Please, Enter the"+loc+" details", Toast.LENGTH_SHORT).show();
        locationNo.setText(loc);

        Calendar calendar= Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        timeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeF.setText(hourOfDay+":" +minute);
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }
        });

        timeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeT.setText(hourOfDay+":" +minute);
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(LocationInfo.this);
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                if (check()) {
                    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("DoctorProfile").child(FirebaseAuth.getInstance().getUid());
                    HospitalLocation hospitalLocation = new HospitalLocation(hospitalName,hosAddress,hosCity,hosTime);
                    assert loc != null;
                    databaseReference.child("HospitalLocation").child(loc).setValue(hospitalLocation);
                    finish();
                    progressDialog.dismiss();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LocationInfo.this , "DETAILS UPLOADED UNSUCCESSFULLY ",Toast.LENGTH_SHORT).show();

                }
                }
        });
    }
    private boolean check() {

        boolean flag = true;
        hospitalName = hosName.getText().toString();
        hosAddress = address.getText().toString();
        hosCity = city.getText().toString();
        hosTime = timeF.getText().toString()+" - "+timeT.getText().toString();
        if(hospitalName.isEmpty() || hosAddress.isEmpty() || hosCity.isEmpty()|| hosTime.isEmpty() ){
            flag= false;
        }
        return flag;
    }
}
