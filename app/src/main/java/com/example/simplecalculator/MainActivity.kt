package com.example.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.simplecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onDigitClick(view: View) {
        if (stateError){
            binding.dataTv.text = (view as Button).text
            stateError = false
        }
        else{
            binding.dataTv.append((view as Button).text)
        }

        lastNumeric = true
        OnEqual()
    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }

    fun onBackClick(view: View) {
         binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try {
            val lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()){
                OnEqual()
            }
        }
        catch(e: Exception){
                binding.resultTv.text = ""
                binding.resultTv.visibility = View.GONE
                Log.e("Last Error",e.toString())
        }
    }

    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            OnEqual()
        }
    }

    fun onEqualClick(view: View) {
        OnEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
    }

    fun onAllClearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        binding.resultTv.visibility = View.GONE
    }

    fun OnEqual(){
        if (lastNumeric && !stateError){
            val txt = binding.dataTv.text.toString()

            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=${result.toString()}"
            }
            catch (ex: ArithmeticException){
                Log.e("Evalute error", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}