package com.test.ocbc.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import com.test.ocbc.R
import com.test.ocbc.databinding.DialogProgressBinding

open class ProgressDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogProgressBinding

    private val dismissHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProgressBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    fun setMessage(message: CharSequence?) {
        if (message == null) binding.message.visibility = View.GONE
        binding.message.text = message
    }

    override fun show() {
        if (isOnRefreshing) return
        isOnRefreshing = true
        super.show()
    }

    private var isOnRefreshing = false
    fun setRefreshing(show: Boolean) {
        dismissHandler.removeCallbacks(this::dismiss)
        if (show)
            show()
        else
            dismissHandler.postDelayed(this::dismiss, 500)
    }

    override fun dismiss() {
        if (isOnRefreshing.not()) return
        isOnRefreshing = false
        super.dismiss()
    }

}