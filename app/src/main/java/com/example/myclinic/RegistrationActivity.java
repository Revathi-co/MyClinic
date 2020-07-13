package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText userName;
    private EditText userAge;
    private EditText userEmail;
    private EditText userPhone;
    private EditText userPassword;
    private Button userRegister;
    private TextView userLogin;
    private RadioGroup Gender;
    private RadioButton gen;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String name;
    String age;
    String email;
    String phone;
    String gender;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    //validate
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String current_id = firebaseAuth.getCurrentUser().getUid();
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                System.out.println(token_id);
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", token_id);
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                firebaseFirestore.collection("Doctors").document(current_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sendEmailVerification();
                                        progressDialog.dismiss();
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

            }
        });
    }

    private void setupUIViews() {
        userName = (EditText)findViewById(R.id.etRName);
        userAge = (EditText)findViewById(R.id.etAge);
        userEmail = (EditText)findViewById(R.id.etEmail);
        userPassword = (EditText)findViewById(R.id.etPassword);
        userPhone = (EditText)findViewById(R.id.etPhoneNo);
        userLogin = (TextView)findViewById(R.id.tvSign);
        userRegister = (Button)findViewById(R.id.btnUpdate);
        Gender = (RadioGroup)findViewById(R.id.rbtnGender);

        progressDialog = new ProgressDialog(this);
    }

    private Boolean validate() {

        Boolean result = false;

        name = userName.getText().toString();
        age = userAge.getText().toString();
        email = userEmail.getText().toString();
        phone = userPhone.getText().toString();
        int radioId = Gender.getCheckedRadioButtonId();
        gen=findViewById(radioId);
        gender = gen.getText().toString();


        if(name.isEmpty() || age.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this , "please enter all the details",Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }
    public void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this,"successfully registered,verify mail sent",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (RegistrationActivity.this, DoctorInfo.class);
                        intent.putExtra("DoctorID",id);
                        intent.putExtra("DoctorName",name);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegistrationActivity.this,"verification mail has'nt been sent",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
    private void sendUserData(){
        DoctorProfile doctorProfile = new DoctorProfile(name,age,email,gender,phone);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DoctorProfile");
        id = firebaseAuth.getUid();
        assert id != null;
        myRef.child(id).setValue(doctorProfile);
    }
}


