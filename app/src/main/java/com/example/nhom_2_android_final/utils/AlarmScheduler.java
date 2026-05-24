package com.example.nhom_2_android_final.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmScheduler {

    private static final long INTERVAL_4_HOURS = 4 * 60 * 60 * 1000L;

    public static void scheduleStudyReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("type", "STUDY_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Báo lần đầu sau 4 tiếng kể từ lúc thiết lập, sau đó lặp lại mỗi 4 tiếng
        long triggerAtMillis = System.currentTimeMillis() + INTERVAL_4_HOURS;

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    INTERVAL_4_HOURS,
                    pendingIntent
            );
        }
    }

    public static void scheduleDailyTip(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("type", "DAILY_TIP");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Báo lần đầu sau 2 tiếng (để lệch giờ với nhắc nhở học tập), sau đó lặp lại mỗi 4 tiếng
        long triggerAtMillis = System.currentTimeMillis() + (INTERVAL_4_HOURS / 2);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    INTERVAL_4_HOURS,
                    pendingIntent
            );
        }
    }
}
