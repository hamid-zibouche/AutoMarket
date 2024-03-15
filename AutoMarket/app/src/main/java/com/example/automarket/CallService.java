package com.example.automarket;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class CallService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Vérifiez s'il y a un numéro de téléphone à appeler
        String phoneNumber = intent.getStringExtra("phone_number");
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }
        stopSelf(); // Arrêtez le service après l'appel
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
