package com.example.firesbaseemlkit.pv_oxy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity {

    EditText patient_name, hospital_name, spo2, heart_rate, body_temp, mrt, blood_pressure, rr;
    AppCompatButton pdf_creator, print_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Oxygen Report");



        patient_name = findViewById(R.id.patient_name);
        hospital_name = findViewById(R.id.hospital_name);
        spo2 = findViewById(R.id.spo2);
        heart_rate = findViewById(R.id.heart_rate);
        body_temp = findViewById(R.id.body_temp);
        mrt = findViewById(R.id.mrt);
        blood_pressure = findViewById(R.id.blood_pressure);
        rr = findViewById(R.id.rr);

        pdf_creator = findViewById(R.id.pdf);

        //Asking user to give access to external storage for saving pdf.
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        //click listener
        pdf_creator.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createPdf();
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPdf(){
        String p_name = patient_name.getText().toString().trim();
        String h_name = hospital_name.getText().toString().trim();
        String spo = spo2.getText().toString().trim();
        String h_rate = heart_rate.getText().toString().trim();
        String b_temp = body_temp.getText().toString().trim();
        String machine_run_rate = mrt.getText().toString().trim();
        String b_pressure = blood_pressure.getText().toString().trim();
        String run_rate = rr.getText().toString().trim();

        String[][] info = new String[][]{
                {"Patient Name", p_name},
                {"Hospital Name", h_name},
                {"SPO2", spo},
                {"Heart Rate", h_rate},
                {"Body Temperature", b_temp},
                {"Machine Run Rate", machine_run_rate},
                {"Blood Pressure", b_pressure},
                {"RR", run_rate}
        };

        //path to store pd
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300, 400, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Canvas canvas = myPage.getCanvas();

        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(12.0f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("OXYGEN REPORT", myPageInfo.getPageWidth()/2, 30, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(6.0f);
        myPaint.setColor(Color.BLACK);

        int startXPos = 10;
        int endXPos = myPageInfo.getPageWidth()-3;
        int startYPos = 80;

        canvas.drawLine(startXPos, 60, endXPos, 60, myPaint);
        for(int i = 0; i<info.length; i++){
            canvas.drawText(info[i][0], startXPos+2, startYPos, myPaint);
            canvas.drawText(info[i][1], startXPos+80, startYPos, myPaint);
            canvas.drawLine(startXPos, startYPos+3, endXPos, startYPos+3, myPaint);
            startYPos += 20;
        }

        canvas.drawLine(10, 60, 10, 223, myPaint);
        canvas.drawLine(80, 60, 80, 223, myPaint);
        canvas.drawLine(endXPos, 60, endXPos, 223, myPaint);

        myPaint.setColor(Color.BLACK);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(LocalDateTime.now().toString().substring(0, 19), 290, 230, myPaint);

        myPdfDocument.finishPage(myPage);

        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // will give access of download folder of android in file manager.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "/Oxygen_report.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(TaskActivity.this, "PDF document is created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.sign_out){
            startActivity(new Intent(TaskActivity.this, MainActivity.class));
            finish();
        }

        return true;
    }
}