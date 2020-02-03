package com.example.rxdemo1

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

fun log(msg: String, tag: String = "MY_TAG") {
    Log.i(tag, msg)
}

fun Context.toast(msg: String, time: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, time).show()
}

fun Context.toast(@StringRes msg: Int, time: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, time).show()
}