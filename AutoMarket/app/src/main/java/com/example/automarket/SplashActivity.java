package com.example.automarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Optionnel : Ajoutez une pause pour afficher l'écran de démarrage pendant quelques secondes
        new Handler().postDelayed(() -> {
            // Après le délai, ouvrez l'activité principale ou toute autre activité
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Terminez l'activité de démarrage pour qu'elle ne revienne pas lorsque vous appuyez sur le bouton Retour
        }, 1000); // Délai de 3 secondes (3000 millisecondes)
    }
}
