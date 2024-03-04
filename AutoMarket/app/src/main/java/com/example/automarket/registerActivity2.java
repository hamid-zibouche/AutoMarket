package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import Controller.DatabaseHandler;


public class registerActivity2 extends AppCompatActivity {

    DatabaseHandler myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        myDB = new DatabaseHandler(this);
    }
}