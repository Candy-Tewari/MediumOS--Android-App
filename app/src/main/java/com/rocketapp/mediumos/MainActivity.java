package com.rocketapp.mediumos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void connect(View view){
        ViewModel viewModel = new ViewModel();
        EditText url = findViewById(R.id.url);
        try {
            System.out.println("================================="+url.getText().toString());
            boolean access = viewModel.getAccess(url.getText().toString());
            if(access){
                Toast.makeText(this, "Connection Established!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Navigator.class));
            }
            else{
                Toast.makeText(this, "Connection cannot be established.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy myCustomizableThread = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myCustomizableThread);
    }
}
