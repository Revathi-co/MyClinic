package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

public class DoorStepActivity extends AppCompatActivity {


    TextView message;
    Button accept, reject;
    String userId;
    String docName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_step);

        message.setText(getIntent().getStringExtra("msg"));
        final String title = getIntent().getStringExtra("title");
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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

                appointmentAcceptNotification(title);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectNotification(title);
            }
        });
    }

    private void appointmentAcceptNotification(String title) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            String docId = FirebaseAuth.getInstance().getUid();
            userId =getIntent().getStringExtra("UserId");
            Map<String,Object> notificationMessage = new HashMap<>();
            notificationMessage.put("Title",title+" Accepted");
            notificationMessage.put("Message",title+" sent to "+ docName+" has been Accepted. Please, send your location");
            notificationMessage.put("from",docId);
            firebaseFirestore.collection("Users/"+ userId+ "/Notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DoorStepActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                    }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DoorStepActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void rejectNotification(String Title) {

        final String docId = FirebaseAuth.getInstance().getUid();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String message = ( Title+" sent to "+ docName+" has been cancelled, please search for other Doctor");
        Map<String,Object> notificationMessage = new HashMap<>();
        notificationMessage.put("Title", Title+" cancelled");
        notificationMessage.put("Message",message);
        notificationMessage.put("from",docId);
        userId = getIntent().getStringExtra("UserId");
        firebaseFirestore.collection("Users/"+ userId+ "/Notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DoorStepActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoorStepActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
