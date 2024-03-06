package com.example.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.automarket.Model.Annonce;
import com.example.automarket.domain.PopularDomain;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent != null) {
            Annonce objet = (Annonce) intent.getSerializableExtra("objet");
            String title = intent.getStringExtra("title");
            int price = intent.getIntExtra("price", 0);
            int score = intent.getIntExtra("score", 0);

            // Afficher les données dans les TextViews de votre layout
            TextView titleTextView = findViewById(R.id.textView2);
            TextView priceTextView = findViewById(R.id.textView3);
            TextView scoreTextView = findViewById(R.id.textView4);

            titleTextView.setText(objet.toString());
            priceTextView.setText(String.valueOf(objet.getPrix()) + "€");
            scoreTextView.setText(objet.getDateCreation());
        }
    }
}