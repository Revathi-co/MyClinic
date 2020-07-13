package com.example.myclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class WorkLocation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listView;
    String[] doctorLocation;

    private Button saveLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveLocations = findViewById(R.id.btnSaveLocation);
        Toast.makeText(this, "Please, select the work location", Toast.LENGTH_SHORT).show();

        doctorLocation = new String[3];
        Resources resources = getResources();
        doctorLocation = resources.getStringArray(R.array.locations);
        listView = findViewById(R.id.locationFrame);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.get_location, R.id.tvLocation, doctorLocation);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkLocation.this, LocationInfo.class);
                intent.putExtra("location",doctorLocation[position]);
                startActivity(intent);
            }
        });


        saveLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkLocation.this,HomeActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


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
