package pl.wsei.pam.lab03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import androidx.gridlayout.widget.GridLayout

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab03)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.memory)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mBoard = findViewById(R.id.memory)
        val columns = intent.getIntExtra("columns", 3)
        val rows = intent.getIntExtra("rows", 4)
        mBoard.columnCount = columns
        mBoard.rowCount = rows

        val boardView = MemoryBoardView(mBoard, columns, rows)
        boardView.setOnGameChangeListener { event ->
            when (event.state) {
                GameStates.Match -> {
                    event.tiles.forEach { it.removeOnClickListener() }
                }
                GameStates.NoMatch -> {
                    // hide tiles after a delay
                    mBoard.postDelayed({
                        event.tiles.forEach { it.revealed = false }
                    }, 1000)
                }
                GameStates.Finished -> {
                    android.widget.Toast.makeText(this, "Game Finished!", android.widget.Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}