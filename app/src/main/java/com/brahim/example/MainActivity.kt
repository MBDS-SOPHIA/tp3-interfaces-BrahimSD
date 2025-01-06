package com.brahim.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * This activity allows the user to roll a dice and view the result
 * on the screen.
 */
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

        val resultTextView1: TextView = findViewById(R.id.textView)
        val resultTextView2: TextView = findViewById(R.id.textView2)
        val targetNumberEditText: EditText = findViewById(R.id.targetNumber)

        resultTextView1.text = diceRoll1.toString()
        resultTextView2.text = diceRoll2.toString()

        val targetNumber = targetNumberEditText.text.toString().toIntOrNull()
        if (targetNumber != null) {
            val sum = diceRoll1 + diceRoll2
            if (sum == targetNumber) {
                Toast.makeText(this, "Félicitations ! Vous avez trouvé le bon nombre !", Toast.LENGTH_SHORT).show()
                animateDice(resultTextView1, resultTextView2)
            } else {
                Toast.makeText(this, "Désolé, essayez encore.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animateDice(vararg views: TextView) {
        val animation = TranslateAnimation(0f, 0f, 0f, 100f)
        animation.duration = 500
        animation.repeatCount = 5
        animation.repeatMode = TranslateAnimation.REVERSE
        views.forEach { it.startAnimation(animation) }
    }
}

class Dice(private val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}