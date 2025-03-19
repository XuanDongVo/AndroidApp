package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myapplication.receiver.AlarmNoteReceiver

object NotificationHelper {
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, message: String,date: String, timeInMillis: Long, requestCode: Int , noteId:Int) {
        Log.d("AlarmNoteReceiver", "Received: message=$message, requestCode=$requestCode, date=$date")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmNoteReceiver::class.java).apply {
            putExtra("NOTICE_MESSAGE", message)
            putExtra("NOTICE_DATE", date)
            putExtra("NOTE_ID",noteId)
            putExtra("REQUEST_CODE", requestCode)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }
}