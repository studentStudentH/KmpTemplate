package com.example.kmptemplate.util

import co.touchlab.kermit.Logger

object KermitLogger {
    fun d(
        tag: String,
        message: () -> String,
    ) {
        Logger.d(tag) { message() }
    }

    fun i(
        tag: String,
        message: () -> String,
    ) {
        Logger.i(tag) { message() }
    }

    fun w(
        tag: String,
        message: () -> String,
    ) {
        Logger.w(tag) { message() }
    }

    fun e(
        tag: String,
        message: () -> String,
    ) {
        Logger.e(tag) { message() }
    }
}