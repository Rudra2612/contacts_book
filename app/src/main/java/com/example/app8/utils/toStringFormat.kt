package com.example.app8.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun Date.toStringFormat(): String {
    return SimpleDateFormat("dd-MMM-yyyy").format(this)
}