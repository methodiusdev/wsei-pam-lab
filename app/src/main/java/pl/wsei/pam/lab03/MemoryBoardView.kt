package pl.wsei.pam.lab03

import android.view.View
import android.widget.ImageButton
import androidx.gridlayout.widget.GridLayout
import java.util.Stack
import pl.wsei.pam.lab01.R

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.ic_puzzle,
        R.drawable.ic_heart,
        R.drawable.ic_star,
        R.drawable.ic_launcher_foreground,
        // dodaj kolejne identyfikatory utworzonych ikon
    )
    private val deckResource: Int = R.drawable.deck
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    init {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            val numPairs = (cols * rows) / 2
            for (i in 0 until numPairs) {
                it.add(icons[i % icons.size])
                it.add(icons[i % icons.size])
            }
            it.shuffle()
        }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val index = row * cols + col
                val button = ImageButton(gridLayout.context).apply {
                    tag = "${row}x${col}"
                    val params = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        setGravity(android.view.Gravity.CENTER)
                        columnSpec = GridLayout.spec(col, 1, 1f)
                        rowSpec = GridLayout.spec(row, 1, 1f)
                    }
                    layoutParams = params
                }
                gridLayout.addView(button)
                addTile(button, shuffledIcons[index])
            }
        }
    }

    private fun onClickTile(v: View) {
        val tag = v.tag as String
        val tile = tiles[tag] ?: return

        if (tile.revealed || matchedPair.size >= 2) return

        tile.revealed = true
        matchedPair.push(tile)

        val matchResult = logic.process {
            tile.tileResource
        }

        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
}