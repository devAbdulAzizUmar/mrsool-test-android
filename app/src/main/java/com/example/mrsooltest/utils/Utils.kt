package com.example.mrsooltest.utils

import java.util.*

object Utils {
    fun getAge(birthDate: Long): Long {

        val birthDate = Date(birthDate)
        val currentDate = Calendar.getInstance().time
        val difference = currentDate.time - birthDate.time
        val seconds = difference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days / 365
    }

}