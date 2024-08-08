package com.example.iphonecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.iphonecalculator.databinding.ActivitymainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivitymainBinding

    private var currentInput: String = ""
    private var previousInput: String = ""
    private var currentOperator: Char? = null
    private var shouldClearDisplay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitymainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()
    }

    private fun setupNumberButtons() {
        val numbers = listOf(
            binding.zero, binding.one, binding.two,
            binding.three, binding.four, binding.five,
            binding.six, binding.seven, binding.eight,
            binding.nine
        )
        numbers.forEach { button ->
            button.setOnClickListener {
                if (shouldClearDisplay) {
                    currentInput = ""
                    shouldClearDisplay = false
                }
                currentInput += button.text
                binding.result.text = currentInput
            }
        }
    }

    private fun setupOperatorButtons() {
        val operators = listOf(
            binding.plus, binding.minus, binding.mul, binding.div
        )
        operators.forEach { button ->
            button.setOnClickListener {
                if (currentInput.isNotEmpty()) {
                    if (previousInput.isNotEmpty()) {
                        calculateResult()
                    } else {
                        previousInput = currentInput
                    }
                    currentOperator = when (button.id) {
                        binding.plus.id -> '+'
                        binding.minus.id -> '-'
                        binding.mul.id -> '×'
                        binding.div.id -> '÷'
                        else -> null
                    }
                    currentInput = ""
                    binding.result.text = "$previousInput $currentOperator"
                    shouldClearDisplay = true
                }
            }
        }
    }

    private fun setupSpecialButtons() {
        binding.clear.setOnClickListener {
            clearAll()
        }

        binding.equal.setOnClickListener {
            calculateResult()
        }

        binding.floatpoint.setOnClickListener {
            if (!currentInput.contains(".")) {
                currentInput += "."
                binding.result.text = currentInput
            }
        }

        binding.sign.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = if (currentInput.startsWith("-")) {
                    currentInput.removePrefix("-")
                } else {
                    "-$currentInput"
                }
                binding.result.text = currentInput
            }
        }

        binding.percent.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = (currentInput.toDouble() / 100).toString()
                binding.result.text = currentInput
                shouldClearDisplay = true
            }
        }
    }

    private fun calculateResult() {
        if (previousInput.isNotEmpty() && currentInput.isNotEmpty() && currentOperator != null) {
            val result = when (currentOperator) {
                '+' -> previousInput.toDouble() + currentInput.toDouble()
                '-' -> previousInput.toDouble() - currentInput.toDouble()
                '×' -> previousInput.toDouble() * currentInput.toDouble()
                '÷' -> previousInput.toDouble() / currentInput.toDouble()
                else -> 0.0
            }

            val formattedResult = if (result == result.toInt().toDouble()) {
                result.toInt().toString()
            } else {
                result.toString().trimEnd('0').trimEnd('.')
            }

            binding.result.text = formattedResult
            previousInput = formattedResult
            currentInput = formattedResult
            currentOperator = null
            shouldClearDisplay = false
        }
    }

    private fun clearAll() {
        currentInput = ""
        previousInput = ""
        currentOperator = null
        shouldClearDisplay = false
        binding.result.text = "0"
    }
}
