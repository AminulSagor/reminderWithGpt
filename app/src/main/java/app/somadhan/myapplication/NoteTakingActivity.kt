package app.somadhan.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Somadhan.myapplication.R
import com.Somadhan.myapplication.databinding.ActivityNoteTakingBinding
import java.util.Calendar

class NoteTakingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNoteTakingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
            insets
        }

        binding.reminderButton.setOnClickListener {

            showDatePicker()

        }


    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                showTimePicker()

            }, year, month, dayOfMonth
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker() {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->

                val selectedTime = "$selectedHour:$selectedMinute"
                showPrioritySelectionDialog()

            },
            hour, minute, true
        )


        timePickerDialog.show()
    }


    private fun showPrioritySelectionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_priority_selection, null)


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        val mediumCheckBox = dialogView.findViewById<CheckBox>(R.id.mediumCheckBox)
        val highCheckBox = dialogView.findViewById<CheckBox>(R.id.highCheckBox)
        val lowCheckBox = dialogView.findViewById<CheckBox>(R.id.lowCheckBox)
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancelButton)
        val saveButton = dialogView.findViewById<TextView>(R.id.saveButton)


        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        saveButton.setOnClickListener {

            dialog.dismiss()
        }


        dialog.show()
    }


}

