package com.example.automarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.automarket.Controller.DatabaseHandler;
import com.example.automarket.Model.Annonce;
import com.example.automarket.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAnnonce extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 102;

    private EditText etMarque, etModele, etBoite, etEnergie, etMoteur, etKilometrage, etCouleur, etAnnee, etPrix, etCommentaire, etAdresse;
    private Button btnAddAnnonce;

    private DatabaseHandler dbHandler;
    private ImageView imageView;
    private Uri imageUri;
    private Bitmap defaultImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annonce);

        dbHandler = new DatabaseHandler(this);

        etMarque = findViewById(R.id.etMarque);
        etModele = findViewById(R.id.etModele);
        etBoite = findViewById(R.id.etBoite);
        etEnergie = findViewById(R.id.etEnergie);
        etMoteur = findViewById(R.id.etMoteur);
        etKilometrage = findViewById(R.id.etKilometrage);
        etCouleur = findViewById(R.id.etCouleur);
        etAnnee = findViewById(R.id.etAnnee);
        etPrix = findViewById(R.id.etPrix);
        etCommentaire = findViewById(R.id.etCommentaire);
        etAdresse = findViewById(R.id.etAdresse);

        btnAddAnnonce = findViewById(R.id.btnAddAnnonce);
        ImageButton btnRetour = findViewById(R.id.btnBack);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> showImageDialog());

        // Charger l'image par défaut depuis les ressources drawable
        defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.addvehicule);

        btnRetour.setOnClickListener(v -> confirmeBack());

        btnAddAnnonce.setOnClickListener(v -> addAnnonce());

        requestStoragePermission();
    }

    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir une photo");
        String[] options = {"Prendre une photo", "Choisir depuis la galerie"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent();
                } else {
                    requestCameraPermission();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_IMAGE);
            }
        });
        builder.show();
    }

    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                imageUri = getImageUri(imageBitmap);
            } else if (requestCode == REQUEST_SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                imageUri = selectedImageUri;
            }
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void addAnnonce() {
        String marque = etMarque.getText().toString().trim();
        String modele = etModele.getText().toString().trim();
        String boite = etBoite.getText().toString().trim();
        String energie = etEnergie.getText().toString().trim();
        String moteur = etMoteur.getText().toString().trim();
        String couleur = etCouleur.getText().toString().trim();
        String commentaire = etCommentaire.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();

        String kilometrageStr = etKilometrage.getText().toString().trim();
        String anneeStr = etAnnee.getText().toString().trim();
        String prixStr = etPrix.getText().toString().trim();

        if (!marque.isEmpty() && !modele.isEmpty() && !boite.isEmpty() && !energie.isEmpty() && !moteur.isEmpty()
                && !couleur.isEmpty() && !commentaire.isEmpty() && !adresse.isEmpty()
                && !kilometrageStr.isEmpty() && !anneeStr.isEmpty() && !prixStr.isEmpty()) {

            try {
                int kilometrage = Integer.parseInt(kilometrageStr);
                int annee = Integer.parseInt(anneeStr);
                double prix = Double.parseDouble(prixStr);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Êtes-vous sûr de vouloir ajouter cette annonce ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                        String dateStr = dateFormat.format(date);

                        String imageUriStr;
                        if (imageUri != null) {
                            imageUriStr = imageUri.toString();
                        } else {
                            // Utiliser l'image par défaut si aucune image n'a été sélectionnée
                            imageUriStr = convertBitmapToBase64(defaultImageBitmap);
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int userId = sharedPreferences.getInt(Utils.KEY_ID,0);

                        Annonce annonce = new Annonce(marque, modele, boite, energie, moteur, kilometrage,
                                couleur, annee, prix, imageUriStr, commentaire, adresse, dateStr,userId);

                        dbHandler.addAnnonce(annonce);

                        Toast.makeText(AddAnnonce.this, "Annonce ajoutée avec succès", Toast.LENGTH_SHORT).show();

                        Intent refreshIntent = new Intent(AddAnnonce.this, MainActivity.class);
                        startActivity(refreshIntent);
                        finish();
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ne rien faire, l'utilisateur a annulé
                    }
                });
                builder.show();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Veuillez entrer des valeurs valides pour le kilométrage, l'année et le prix", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmeBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAnnonce.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir quitter sans ajouter l'annonce ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ne rien faire, l'utilisateur a annulé
            }
        });
        builder.show();
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée, vous pouvez maintenant charger les images depuis la galerie
            } else {
                // Permission refusée, vous pouvez informer l'utilisateur ou gérer en conséquence
            }
        }
    }
}
