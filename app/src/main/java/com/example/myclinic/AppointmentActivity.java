package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView message,chooseText;
    Spinner locSpinner;
    Button accept, reject, send;
    String loc;
    String userId;
    String docName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        message = findViewById(R.id.tvMsg);
        chooseText = findViewById(R.id.textView7);
        accept = findViewById(R.id.btnAccept);
        reject = findViewById(R.id.btnReject);
        send = findViewById(R.id.btnSendApp);
        locSpinner = findViewById(R.id.spinnerLoc);

        message.setText(getIntent().getStringExtra("msg"));

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.locations,
                R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locSpinner.setAdapter(adapter);
        locSpinner.setOnItemSelectedListener(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("DoctorProfile").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DoctorProfile docProfile = dataSnapshot.getValue(DoctorProfile.class);
                assert docProfile != null;
                docName = docProfile.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                chooseText.setVisibility(View.VISIBLE);
                locSpinner.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               rejectNotification();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentAcceptNotification();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loc = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void appointmentAcceptNotification() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("DoctorProfile").child(FirebaseAuth.getInstance().getUid())
                .child("HospitalLocation").child(loc);
        final String docId = FirebaseAuth.getInstance().getUid();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalLocation hosLoc = dataSnapshot.getValue(HospitalLocation.class);
                assert hosLoc != null;

                String message = ("Appointment request sent to "+ docName+" has been Accepted.\n Location : "+ hosLoc.getHospitalName() +", "
                + hosLoc.getHospitalAddress()+ ", " +hosLoc.getCity() + "\nTimings :" + hosLoc.getAvailableTime());
                System.out.println(message);
                Map<String,Object> notificationMessage = new HashMap<>();
                notificationMessage.put("Title", "Appointment Accepted");
                notificationMessage.put("Message",message);
                notificationMessage.put("from",docId);
                userId = getIntent().getStringExtra("UserId");
                firebaseFirestore.collection("Users/"+ userId+ "/Notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AppointmentActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppointmentActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void rejectNotification() {


        final String docId = FirebaseAuth.getInstance().getUid();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String message = ("Appointment request sent to "+ docName+" has been cancelled, please search for other Doctor");
        Map<String,Object> notificationMessage = new HashMap<>();
        notificationMessage.put("Title", "Appointment cancelled");
        notificationMessage.put("Message",message);
        notificationMessage.put("from",docId);
        userId = getIntent().getStringExtra("UserId");
        firebaseFirestore.collection("Users/"+ userId+ "/Notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AppointmentActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppointmentActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
