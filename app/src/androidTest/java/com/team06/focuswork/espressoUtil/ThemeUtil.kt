package com.team06.focuswork.espressoUtil

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.R
import java.lang.reflect.Method

object ThemeUtil {
    fun getThemeBackground(context: Context, theme: Int): Int {
        val arr = context.theme.obtainStyledAttributes(
                theme,
                IntArray(1) { R.attr.background })
        val ret = arr.getColor(0, 0)
        arr.recycle()
        return ret
    }
}