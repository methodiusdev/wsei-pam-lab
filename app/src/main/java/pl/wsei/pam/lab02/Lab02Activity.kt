package pl.wsei.pam.lab02

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab02)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.favorites_grid)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onButtonClick(view: View) {
        val tag = view.tag as? String
        if (tag != null) {
            val dimensions = tag.split(" ")
            if (dimensions.size == 2) {
                val rows = dimensions[0]
                val cols = dimensions[1]
                Toast.makeText(this, "Selected board: $rows x $cols", Toast.LENGTH_SHORT).show()
            }
        }
    }
}