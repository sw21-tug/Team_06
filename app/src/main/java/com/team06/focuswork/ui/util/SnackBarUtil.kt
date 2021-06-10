package com.team06.focuswork.ui.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.team06.focuswork.R

object SnackBarUtil {

    fun showSnackBar(view: View, id: Int, activity: Activity) {
        val res = activity.applicationContext.resources
        val snackBar: Snackbar =
            Snackbar.make(view, res.getString(id), Snackbar.LENGTH_LONG)
                .setBackgroundTint(
                    ResourcesCompat.getColor(res, R.color.primary_text, null)
                )
                .setTextColor(ResourcesCompat.getColor(res, R.color.white, null))

        val mTextView =
            snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        mTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER

        val imm: InputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        snackBar.show()
    }
}