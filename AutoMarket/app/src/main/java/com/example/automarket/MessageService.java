package com.example.automarket;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class MessageService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Vérifiez s'il y a un numéro de téléphone et un message à envoyer
        String phoneNumber = intent.getStringExtra("phone_number");
        String message = intent.getStringExtra("message");
        if (phoneNumber != null && !phoneNumber.isEmpty() && message != null && !message.isEmpty()) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", message);
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(smsIntent);
        }
        stopSelf(); // Arrêtez le service après l'envoi du message
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
