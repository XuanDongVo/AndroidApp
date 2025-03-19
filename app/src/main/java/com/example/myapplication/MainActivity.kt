package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myapplication.navHost.NoteApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tạo kênh thông báo (dự phòng)
        createNotificationChannel()
        setContent {
            NoteApp()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "NOTICE_CHANNEL_ID",
                "Notice Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for note reminders"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("MainActivity", "Notification channel created: NOTICE_CHANNEL_ID")
        }
    }


}