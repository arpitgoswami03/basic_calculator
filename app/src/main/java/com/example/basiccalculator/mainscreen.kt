package com.example.basiccalculator

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.basiccalculator.databinding.ActivityMainscreenBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class mainscreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInputChangeListener()

        val buttonIds = arrayOf(
            binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9,
            binding.buttonDot, binding.buttonAdd, binding.buttonSubtract, binding.buttonMultiply,
            binding.buttonDivide, binding.buttonBr1, binding.buttonBr2, binding.backspace, binding.AC,
            binding.buttonEquals

        )

        for (buttonId in buttonIds)
        {
            buttonId.setOnClickListener {
                onButtonClick(it)
            }
        }

        binding.buttonEquals.setOnClickListener {
            onEqualsButtonClick()
        }
    }

    private fun setupInputChangeListener() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Calculate and display result while typing
                val expression = s.toString()
                try {
                    val result = evaluateExpression(expression)
                    binding.resultTextView.setText(result.toString())
                } catch (e: Exception) {
                    // Handle invalid expressions
                    binding.resultTextView.text.clear()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()
        val currentText = binding.editText.text.toString()

        when (buttonText) {
            "AC" -> binding.editText.setText("")
            "C" -> {
                if (currentText.isNotEmpty()) {
                    binding.editText.setText(currentText.dropLast(1))
                }
            }
            else -> binding.editText.setText(currentText + buttonText)
        }
    }

    private fun onEqualsButtonClick() {
        val currentText = binding.editText.text.toString()
        try {
            val result = evaluateExpression(currentText)
            val resultInt = result.toInt()
            binding.resultTextView.text = Editable.Factory.getInstance().newEditable(resultInt.toString())
            binding.editText.text = Editable.Factory.getInstance().newEditable(resultInt.toString())
        } catch (e: Exception) {
            binding.editText.setText("Error: ${e.message}")
        }
    }

    private fun evaluateExpression(expression: String): Int {
        return ExpressionBuilder(expression).build().evaluate().toInt()
    }

}