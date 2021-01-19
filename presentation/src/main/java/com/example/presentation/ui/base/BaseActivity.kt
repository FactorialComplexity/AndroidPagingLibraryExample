package com.example.presentation.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(@LayoutRes layoutResId: Int) : AppCompatActivity(layoutResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    abstract fun initUI()

    protected fun showToast(text: String, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, text, length).show()

    protected fun showAlert(title: String, text: String): AlertDialog =
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(text)
            .show()
}
