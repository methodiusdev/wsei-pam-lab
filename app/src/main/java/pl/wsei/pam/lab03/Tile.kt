package pl.wsei.pam.lab03

import android.widget.ImageButton

data class Tile(val button: ImageButton, val tileResource: Int, val deckResource: Int) {
    private var _revealed: Boolean = false
    var revealed: Boolean
        get() = _revealed
        set(value) {
            _revealed = value
            if (value) {
                button.setImageResource(tileResource)
            } else {
                button.setImageResource(deckResource)
            }
        }

    init {
        button.setImageResource(deckResource)
    }

    fun removeOnClickListener() {
        button.setOnClickListener(null)
    }
}
