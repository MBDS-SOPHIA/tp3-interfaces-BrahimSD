package com.brahim.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)

        targetNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    rollDice()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll1 = dice.roll()
        val diceRoll2 = dice.roll()

        val diceImageView1: ImageView = findViewById(R.id.diceImageView1)
        val diceImageView2: ImageView = findViewById(R.id.diceImageView2)
        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)

        diceImageView1.setImageResource(getDiceImage(diceRoll1))
        diceImageView2.setImageResource(getDiceImage(diceRoll2))

        val targetNumber = targetNumberEditText.text.toString().toIntOrNull()
        if (targetNumber != null) {
            val sum = diceRoll1 + diceRoll2
            if (sum == targetNumber) {
                Toast.makeText(this, "Félicitations ! Vous avez trouvé le bon nombre !", Toast.LENGTH_SHORT).show()
                animateDice(diceImageView1, diceImageView2)
                showConfetti()
            } else {
                Toast.makeText(this, "Désolé, essayez encore.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDiceImage(diceRoll: Int): Int {
        return when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }

    private fun animateDice(vararg views: ImageView) {
        val animation = TranslateAnimation(0f, 0f, 0f, 100f)
        animation.duration = 500
        animation.repeatCount = 5
        animation.repeatMode = TranslateAnimation.REVERSE
        views.forEach { it.startAnimation(animation) }
    }

    private fun showConfetti() {
        val konfettiView: KonfettiView = findViewById(R.id.konfettiView)
        konfettiView.start(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def, 0x29cdff),
                position = Position.Relative(0.5, 0.3),
                size = listOf(Size.SMALL, Size.LARGE),
                timeToLive = 3000L,
                shapes = listOf(Shape.Circle, Shape.Square),
                emitter = Emitter(duration = 1000, TimeUnit.MILLISECONDS).perSecond(100)
            )
        )
    }
}

class Dice(private val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}