package com.amplify.amplify;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

public class InitApplication extends Application {
    private static volatile InitApplication mInstance = null;

    public static InitApplication getGlobalApplicationContext() {
        if (mInstance == null) {
            throw new IllegalStateException("This Application does not GlobalAuthHelper");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createNotificationChannel(mInstance.getApplicationContext());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("APP ID") // Required for Analytics.
                .setProjectId("PROJECT ID") // Required for Firebase Installations.
                .setApiKey("GOOGLE API KEY") // Required for Auth.
                .build();
        FirebaseApp.initializeApp(this, options, "FIREBASE APP NAME");
    }


    private void createNotificationChannel(Context context) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("10001", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // Configure the notification channel
                notificationChannel.setDescription("Firebase 푸시알림");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{200, 100, 200});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            FirebaseMessaging.getInstance().subscribeToTopic("notify").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"notify topic 구독",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NullPointerException nullException) {
            Toast.makeText(context, "푸시 알림 채널 생성에 실패했습니다. 앱을 재실행하거나 재설치해주세요.", Toast.LENGTH_SHORT).show();
            nullException.printStackTrace();
        }
    }
}