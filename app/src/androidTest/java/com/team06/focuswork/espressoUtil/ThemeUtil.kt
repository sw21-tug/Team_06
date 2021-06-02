package com.team06.focuswork.espressoUtil

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.internal.util.Checks
import androidx.test.platform.app.InstrumentationRegistry
import com.team06.focuswork.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.lang.reflect.Method

object ThemeUtil {
    /* Nicely, this is unable to read any attributes
    val a = context.theme.obtainStyledAttributes(
        R.style.Theme_FocusWork_Dark,
        intArrayOf(R.attr.backgroundColor))
    val t = TypedValue()
    a.getValue(0, t)
    a

     */
    fun getThemeBackground(context: Context, theme: Int): Int {
        val arr = context.theme.obtainStyledAttributes(
                theme,
                IntArray(1) { R.attr.background })
        val ret = arr.getColor(0, 0)
        arr.recycle()
        return ret
    }

    fun withBackgroundColor(color: Int): Matcher<View?>? {
        Checks.checkNotNull(color)
        return object : BoundedMatcher<View?, View>(View::class.java) {
            var actual = 0
            override fun matchesSafely(view: View): Boolean {
                //val localBinding = dynamicBinding as FragmentDayBinding
                //val root = view.rootView
                //val draw = root.background as ColorDrawable
                //String.format("#%06X", (0xFFFFFF and draw.color))

                actual = (view.rootView.background as ColorDrawable).color
                Log.d("ThemeTest", "Color is: $color and actual is: $actual")
                return color == actual
            }

            override fun describeTo(description: Description) {
                description.appendText("with Background color: Got "
                    + Integer.toHexString(actual) + " but expected " + Integer.toHexString(color))
            }
        }
    }
}