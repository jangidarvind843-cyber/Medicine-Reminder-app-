package com.example.medreminder

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<MedicineModel>()
    private lateinit var adapter: MedicineAdapter

    private val smsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) Toast.makeText(this, "SMS Permission Required!", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsPermission.launch(Manifest.permission.SEND_SMS)

        val listView = findViewById<ListView>(R.id.listView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        adapter = MedicineAdapter(this, list) { index ->
            list[index].taken = true
            adapter.notifyDataSetChanged()
        }

        listView.adapter = adapter

        btnAdd.setOnClickListener {
            AddMedicineDialog(this) { name, time, guardian ->
                val med = MedicineModel(name, time, guardian)
                list.add(med)
                adapter.notifyDataSetChanged()
                scheduleAlarm(med)
            }.show()
        }
    }

    private fun scheduleAlarm(med: MedicineModel) {
        val (hour, minute) = med.time.split(":").map { it.toInt() }

        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)

        if (cal.timeInMillis < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("name", med.name)
        intent.putExtra("guardian", med.guardian)

        val pi = PendingIntent.getBroadcast(
            this,
            med.name.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE else 0
        )

        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)

        Toast.makeText(this, "Alarm Set for ${med.time}", Toast.LENGTH_SHORT).show()
    }
}
