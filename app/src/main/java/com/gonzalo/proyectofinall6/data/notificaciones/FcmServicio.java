package com.gonzalo.proyectofinall6.data.notificaciones;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gonzalo.proyectofinall6.R;
import com.gonzalo.proyectofinall6.data.repositorios.NotificacionesRepository;
import com.gonzalo.proyectofinall6.ui.MyApp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmServicio extends FirebaseMessagingService {

    private static final String TAG = "FcmServicio";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        showNotification(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        new NotificacionesRepository(getApplicationContext()).registrarTokenFcm(token);
    }

    private void showNotification(@NonNull RemoteMessage message) {
        RemoteMessage.Notification notification = message.getNotification();
        String title = notification != null ? notification.getTitle() : null;
        String body = notification != null ? notification.getBody() : null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_diente)
                .setAutoCancel(true);

        if (title != null) {
            builder.setContentTitle(title);
        }
        if (body != null) {
            builder.setContentText(body);
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());
        Log.d(TAG, "Notificación FCM recibida");
    }
}
