package com.example.presentation.ui.extensions

import android.os.SystemClock
import android.view.View

fun View.clickWithDebounce(debounceTime: Long = 400L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime = 0L

        override fun onClick(v: View) {
            SystemClock.elapsedRealtime().takeIf { it - lastClickTime > debounceTime }?.run {
                action()
                lastClickTime = this
            }
        }
    })
}
