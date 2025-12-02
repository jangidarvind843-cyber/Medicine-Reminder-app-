package com.example.medreminder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class MedicineAdapter(
    val context: Context,
    val list: MutableList<MedicineModel>,
    val onTakenClicked: (Int) -> Unit
) : BaseAdapter() {

    override fun getCount() = list.size
    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false)

        v.findViewById<TextView>(R.id.tvName).text = list[i].name
        v.findViewById<TextView>(R.id.tvTime).text = "Time: ${list[i].time}"
        v.findViewById<TextView>(R.id.tvGuardian).text = "Guardian: ${list[i].guardian}"

        val btnTaken = v.findViewById<Button>(R.id.btnTaken)
        btnTaken.setOnClickListener {
            onTakenClicked(i)
        }

        return v
    }
}
