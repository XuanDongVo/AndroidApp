package com.example.myapplication.receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.MainActivity

class AlarmNoteReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmNoteReceiver", "onReceive called")

        val message = intent.getStringExtra("NOTICE_MESSAGE") ?: "Bạn có note cần xem!"
        val requestCode = intent.getIntExtra("REQUEST_CODE", 0)
        val noteId = intent.getIntExtra("NOTE_ID", -1)
        val date = intent.getStringExtra("NOTICE_DATE") ?: "Unknown date"

        // Tạo kênh thông báo nếu chưa tồn tại
        createNotificationChannel(context)

        // Kiểm tra quyền thông báo
        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) {
            Log.w("AlarmNoteReceiver", "Notifications are disabled. Cannot show notification.")
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            return
        }

        // Intent để mở MainActivity và truyền dữ liệu
        val openIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("NOTE_ID", noteId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "NOTICE_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nhắc nhở")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        Log.d("AlarmNoteReceiver", "Sending notification with requestCode=$requestCode")
        notificationManager.notify(requestCode, notification)
        Log.d("AlarmNoteReceiver", "Notification sent successfully")
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "NOTICE_CHANNEL_ID",
                "Notice Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for note reminders"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("AlarmNoteReceiver", "Notification channel created in AlarmNoteReceiver: NOTICE_CHANNEL_ID")
        }
    }
}