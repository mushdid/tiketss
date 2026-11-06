package com.example.tikets

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    private val departCal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spFrom = findViewById<Spinner>(R.id.spFrom)
        val spTo = findViewById<Spinner>(R.id.spTo)
        val etDepart = findViewById<TextInputEditText>(R.id.etDepart)
        val etReturn = findViewById<TextInputEditText>(R.id.etReturn)
        val tilDepart = findViewById<TextInputLayout>(R.id.tilDepart)
        val tilReturn = findViewById<TextInputLayout>(R.id.tilReturn)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        spTo.setSelection(1) // по умолчанию города разные

        // Виджет выбора даты (DatePickerDialog) для обоих полей
        val openDepart = { showDatePicker(etDepart, null) }
        etDepart.setOnClickListener { openDepart() }
        tilDepart.setEndIconOnClickListener { openDepart() }

        val openReturn = {
            // дата прилёта не может быть раньше даты вылета
            val min = if (etDepart.text.isNullOrEmpty()) null else departCal.timeInMillis
            showDatePicker(etReturn, min)
        }
        etReturn.setOnClickListener { openReturn() }
        tilReturn.setEndIconOnClickListener { openReturn() }

        btnSearch.setOnClickListener {
            when {
                etDepart.text.isNullOrEmpty() || etReturn.text.isNullOrEmpty() ->
                    Toast.makeText(this, R.string.err_dates, Toast.LENGTH_SHORT).show()
                spFrom.selectedItemPosition == spTo.selectedItemPosition ->
                    Toast.makeText(this, R.string.err_same_city, Toast.LENGTH_SHORT).show()
                else -> {
                    val adults = findViewById<TextInputEditText>(R.id.etAdults).text.toString()
                    val children = findViewById<TextInputEditText>(R.id.etChildren).text.toString()
                    val infants = findViewById<TextInputEditText>(R.id.etInfants).text.toString()
                    val msg = "${spFrom.selectedItem} → ${spTo.selectedItem}, " +
                            "${etDepart.text} – ${etReturn.text}, " +
                            "пассажиры: $adults/$children/$infants"
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDatePicker(target: TextInputEditText, minMillis: Long?) {
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            { _, y, m, d ->
                cal.set(y, m, d)
                target.setText(format.format(cal.time))
                if (target.id == R.id.etDepart) departCal.time = cal.time
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = minMillis ?: System.currentTimeMillis() - 1000
        dialog.show()
    }
}