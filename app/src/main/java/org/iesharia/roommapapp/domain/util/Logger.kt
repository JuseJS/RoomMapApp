package org.iesharia.roommapapp.util

import android.util.Log

object Logger {
    private const val TAG = "RoomMapApp"
    private const val DEBUG = true

    fun d(tag: String, message: String) {
        if (DEBUG) Log.d("$TAG:$tag", message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$TAG:$tag", message, throwable)
    }

    fun i(tag: String, message: String) {
        Log.i("$TAG:$tag", message)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w("$TAG:$tag", message, throwable)
    }
}