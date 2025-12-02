package com.example.medreminder

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Button
import android.widget.EditText

class AddMedicineDialog(
    private val context: Context,
    private val onSave: (name: String, time: String, guardian: String) -> Unit
) {

    fun show() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_medicine)

        val etName = dialog.findViewById<EditText>(R.id.etName)
        val etTime = dialog.findViewById<EditText>(R.id.etTime)
        val etGuardian = dialog.findViewById<EditText>(R.id.etGuardian)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        etTime.setOnClickListener {
            val picker = TimePickerDialog(
                context,
                { _, h, m -> etTime.setText(String.format("%02d:%02d", h, m)) },
                12, 0, true
            )
            picker.show()
        }

        btnSave.setOnClickListener {
            onSave(etName.text.toString(), etTime.text.toString(), etGuardian.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }
}
