package com.lonjeztech.crimemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnReportCrime = findViewById(R.id.btn_report_crime);

        btnReportCrime.setOnClickListener(v -> {
            Intent intent = new Intent(this,Report.class);
            startActivity(intent);
        });
    }
}