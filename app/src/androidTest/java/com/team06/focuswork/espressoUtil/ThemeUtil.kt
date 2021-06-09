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
    fun withBackgroundColor(color: Int): Matcher<View?>? {
        Checks.checkNotNull(color)
        return object : BoundedMatcher<View?, View>(View::class.java) {
            var actual = 0
            override fun matchesSafely(view: View): Boolean {
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

    fun withAccentColor(color: Int): Matcher<View?>? {
        Checks.checkNotNull(color)
        return object : BoundedMatcher<View?, View>(View::class.java) {
            var actual = 0
            override fun matchesSafely(view: View): Boolean {
                actual = (view.background as ColorDrawable).color
                return color == actual
            }

            override fun describeTo(description: Description) {
                description.appendText("with Accent color: Got "
                    + Integer.toHexString(actual) + " but expected " + Integer.toHexString(color))
            }
        }
    }
}