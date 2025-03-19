package com.example.myapplication.viewModel

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Reminder
import com.example.myapplication.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    private val reminderDao = AppDatabase.getDatabase(application).reminderDao()
    private val noteDao =AppDatabase.getDatabase(application).noteDao()

    suspend fun insertReminder(noteId: Int, timeInMillis: Long, date: String) {
        val reminder = Reminder(noteId = noteId, timeInMillis = timeInMillis, date = date)
        withContext(Dispatchers.IO) {
            reminderDao.insertReminder(reminder)
            noteDao.setReminderNote(noteId)
        }

        val context = getApplication<Application>().applicationContext

        if (!hasExactAlarmPermission(context)) {
            requestExactAlarmPermission(context)
            return
        }

        // Kiểm tra quyền POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission(context)) {
                requestNotificationPermission(context)
                return
            }
        }

            NotificationHelper.scheduleNotification(
                context,
                "Bạn có ghi chú cần làm",
                date,
                timeInMillis,
                200,
                noteId
            )
        }

    suspend fun modify(noteId: Int, timeInMillis: Long, date: String) {
        reminderDao.modifyReminders(noteId, timeInMillis, date)
    }

private fun hasExactAlarmPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

private fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

private fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    } else {
        true
    }
}

private fun requestNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

}
