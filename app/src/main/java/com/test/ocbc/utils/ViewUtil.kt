package com.test.ocbc.utils

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.test.ocbc.R

object ViewUtil {
    fun Activity.showSnackImageToast(message: String) {
        val snackbar = Snackbar.make(this.window.decorView.rootView, message, Snackbar.LENGTH_LONG)
        val snackBarText =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackBarText.textSize = 14f
        snackBarText.typeface = ResourcesCompat.getFont(this, R.font.barlow_medium)
        snackBarText.setTextColor(ContextCompat.getColor(this, R.color.white))

        snackBarText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_circle_warning
            ), null, null, null
        )
        snackBarText.compoundDrawablePadding =
            this.resources.getDimensionPixelSize(R.dimen.activity_margin_small)
        snackbar.view.translationY = (-64).toPx
        snackbar.show()
    }

    val Number.toPx
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
        )
}