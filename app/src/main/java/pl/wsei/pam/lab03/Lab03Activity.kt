package pl.wsei.pam.lab03

import android.os.Bundle
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import androidx.gridlayout.widget.GridLayout
import java.util.Random

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout
    private var isSound: Boolean = true
    private var completionPlayer: MediaPlayer? = null
    private var negativePlayer: MediaPlayer? = null
    private lateinit var boardView: MemoryBoardView

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

        boardView = MemoryBoardView(mBoard, columns, rows)
        boardView.setOnGameChangeListener { event ->
            when (event.state) {
                GameStates.Match -> {
                    if (isSound) completionPlayer?.start()
                    boardView.setLocked(true)
                    val animators = event.tiles.map { tile ->
                        createMatchAnimator(tile.button)
                    }
                    AnimatorSet().apply {
                        playTogether(animators)
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {
                                event.tiles.forEach { it.removeOnClickListener() }
                                boardView.setLocked(false)
                            }
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                        start()
                    }
                }
                GameStates.NoMatch -> {
                    if (isSound) negativePlayer?.start()
                    boardView.setLocked(true)
                    val animators = event.tiles.map { tile ->
                        createNoMatchAnimator(tile.button)
                    }
                    AnimatorSet().apply {
                        playTogether(animators)
                        startDelay = 500
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {
                                event.tiles.forEach { it.revealed = false }
                                boardView.setLocked(false)
                            }
                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                        start()
                    }
                }
                GameStates.Finished -> {
                    if (isSound) completionPlayer?.start()
                    Toast.makeText(this, "Game Finished!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(this, R.raw.completion)
        negativePlayer = MediaPlayer.create(this, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer?.release()
        negativePlayer?.release()
        completionPlayer = null
        negativePlayer = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.board_activity_menu, menu)
        val soundItem = menu.findItem(R.id.board_activity_sound)
        updateSoundIcon(soundItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.board_activity_sound -> {
                isSound = !isSound
                updateSoundIcon(item)
                Toast.makeText(this, if (isSound) "Sound on" else "Sound off", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSoundIcon(item: MenuItem?) {
        item?.setIcon(if (isSound) R.drawable.baseline_volume_up_24 else R.drawable.baseline_volume_off_24)
    }

    private fun createMatchAnimator(button: ImageButton): Animator {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * button.width
        button.pivotY = random.nextFloat() * button.height

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scalingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scalingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)

        set.apply {
            startDelay = 200
            duration = 1500
            interpolator = DecelerateInterpolator()
            playTogether(rotation, scalingX, scalingY, fade)
        }
        return set
    }

    private fun createNoMatchAnimator(button: ImageButton): Animator {
        val set = AnimatorSet()
        val rotation = ObjectAnimator.ofFloat(button, "rotation", 0f, 10f, -10f, 10f, -10f, 0f)
        set.apply {
            duration = 500
            playTogether(rotation)
        }
        return set
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isSound", isSound)
        outState.putBundle("boardState", boardView.getState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isSound = savedInstanceState.getBoolean("isSound", true)
        val boardState = savedInstanceState.getBundle("boardState")
        if (boardState != null) {
            boardView.setState(boardState)
        }
    }
}
