package com.example.submission5.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.submission5.MainActivity
import com.example.submission5.R
import java.util.*

class DailyReminder : BroadcastReceiver() {
    companion object{
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        showAlarmNotification(context, 101)
    }

    fun setReminderDaily(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 9){
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
    fun cancelReminderDaily(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun showAlarmNotification(context: Context, notifId : Int){
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager Channel"
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.github)
            .setContentTitle("Daily Notification")
            .setContentText("Today")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }
}
