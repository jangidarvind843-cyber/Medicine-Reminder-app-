package com.example.medreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val name = intent.getStringExtra("name") ?: ""
        val guardian = intent.getStringExtra("guardian") ?: ""

        // Prepare worker input
        val data = workDataOf(
            "name" to name,
            "guardian" to guardian
        )

        // Delay 10 minutes before sending SMS *** change to 15 SECONDS for testing ***
        val request = OneTimeWorkRequestBuilder<SendSmsWorker>()
            .setInitialDelay(10, TimeUnit.MINUTES)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)

        Toast.makeText(context, "⏰ Time to take medicine: $name", Toast.LENGTH_LONG).show()
    }
}
