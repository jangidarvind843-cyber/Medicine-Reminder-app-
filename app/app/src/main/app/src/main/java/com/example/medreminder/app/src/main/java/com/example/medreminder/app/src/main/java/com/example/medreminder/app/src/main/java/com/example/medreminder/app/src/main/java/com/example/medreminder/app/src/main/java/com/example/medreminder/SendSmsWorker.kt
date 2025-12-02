package com.example.medreminder

import android.content.Context
import android.telephony.SmsManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class SendSmsWorker(ctx: Context, params: WorkerParameters) :
    Worker(ctx, params) {

    override fun doWork(): Result {

        val name = inputData.getString("name") ?: return Result.failure()
        val guardian = inputData.getString("guardian") ?: return Result.failure()

        val smsMessage = "⚠ Alert: Medicine $name was NOT taken. Please check."

        return try {
            val sms = SmsManager.getDefault()
            sms.sendTextMessage(guardian, null, smsMessage, null, null)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
