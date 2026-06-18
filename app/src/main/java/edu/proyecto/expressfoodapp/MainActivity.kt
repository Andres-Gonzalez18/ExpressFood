package edu.proyecto.expressfoodapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Dejamos el top en 0 para que el fondo verde del header suba hasta el borde superior.
            // El header se encargará de dejar el espacio para la hora/batería.
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }
}