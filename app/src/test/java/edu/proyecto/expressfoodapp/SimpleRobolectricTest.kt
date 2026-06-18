package edu.proyecto.expressfoodapp

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SimpleRobolectricTest {

    @Test
    fun canCreateAndroidViewsWithRobolectric() {
        val context =
            ApplicationProvider.getApplicationContext<android.content.Context>()

        val layout = LinearLayout(context)
        val title = TextView(context)
        val button = Button(context)

        title.text = "ExpressFood"
        button.text = "Continuar con Google"

        layout.addView(title)
        layout.addView(button)

        assertNotNull(layout)
        assertEquals("ExpressFood", title.text.toString())
        assertEquals("Continuar con Google", button.text.toString())
        assertEquals(2, layout.childCount)
    }
}