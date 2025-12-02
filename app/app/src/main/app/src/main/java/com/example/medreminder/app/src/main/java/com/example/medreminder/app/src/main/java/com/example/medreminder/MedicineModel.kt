package com.example.medreminder

data class MedicineModel(
    val name: String,
    val time: String,
    val guardian: String,
    var taken: Boolean = false
)
