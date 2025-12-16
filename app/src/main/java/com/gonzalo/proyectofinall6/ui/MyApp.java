package com.gonzalo.proyectofinall6.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.gonzalo.proyectofinall6.data.repositorios.NotificacionesRepository;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApp extends Application {

    public static final String NOTIFICATION_CHANNEL_ID = "fcm_notifications";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("El token no fue generado");
                return;
            }
            String token = task.getResult();
            System.out.println("El token es: " + token);

            // Registrar token en el backend para poder enviar push al cancelar turnos
            new NotificacionesRepository(getApplicationContext()).registrarTokenFcm(token);
        });
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Notificaciones FCM",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Estas notificaciones van a ser recibidas desde FCM");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
