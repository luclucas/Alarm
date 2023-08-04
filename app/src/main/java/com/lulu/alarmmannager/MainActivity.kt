package com.lulu.alarmmannager

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var btn: AppCompatButton
    private lateinit var timePicker: TimePicker
    private lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.btnAlarmar)
        timePicker = findViewById(R.id.timePicker)
        datePicker = findViewById(R.id.datePicker)


        createNotificationChannel()
        btn.setOnClickListener {
            scheduleNotification()
        }

    }

    fun scheduleNotification() {
        val intent = Intent(this, Notififcation::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            123,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time)
    }

    fun showAlert(time: Long) {
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(this)
        val timeFormat = DateFormat.getTimeFormat(this)

        AlertDialog.Builder(this)
            .setTitle("notificação marcada")
            .setMessage("at: ${dateFormat.format(date)} ${timeFormat.format(date)}")
            .setPositiveButton("Ok") { _, _ -> }
            .show()


    }

    fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "testandoReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("testando", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}