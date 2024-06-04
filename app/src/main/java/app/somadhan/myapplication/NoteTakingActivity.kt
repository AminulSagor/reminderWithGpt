package app.somadhan.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.Somadhan.myapplication.R
import com.Somadhan.myapplication.databinding.ActivityNoteTakingBinding
import app.somadhan.myapplication.database.NoteDatabase
import app.somadhan.myapplication.models.NoteSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class NoteTakingActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNoteTakingBinding.inflate(layoutInflater)
    }

    private lateinit var noteDatabase: NoteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
            insets
        }

        noteDatabase = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "note_database"
        ).build()


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
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time

                showTimePicker(selectedDate)
            }, year, month, dayOfMonth
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker(selectedDate: Date) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = "$selectedHour:$selectedMinute"
                showPrioritySelectionDialog(selectedDate, selectedTime)
            },
            hour, minute, true
        )

        timePickerDialog.show()
    }

    private fun showPrioritySelectionDialog(selectedDate: Date, selectedTime: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_priority_selection, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val widthInDp = 320
        val scale = resources.displayMetrics.density
        val widthInPx = (widthInDp * scale + 0.5f).toInt()

        dialog.setOnShowListener {
            val window = dialog.window
            if (window != null) {
                val params = window.attributes
                params.width = widthInPx
                window.attributes = params
            }
        }

        val mediumCheckBox = dialogView.findViewById<CheckBox>(R.id.mediumCheckBox)
        val highCheckBox = dialogView.findViewById<CheckBox>(R.id.highCheckBox)
        val lowCheckBox = dialogView.findViewById<CheckBox>(R.id.lowCheckBox)
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancelButton)
        val saveButton = dialogView.findViewById<TextView>(R.id.saveButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        saveButton.setOnClickListener {
            val priority = when {
                highCheckBox.isChecked -> "High"
                mediumCheckBox.isChecked -> "Medium"
                lowCheckBox.isChecked -> "Low"
                else -> "Low"
            }

            val title = binding.titleText.text.toString()
            val description = binding.descriptionText.text.toString()

            saveNoteToDatabase(title, description, selectedDate, selectedTime, priority)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveNoteToDatabase(title: String, description: String, date: Date, time: String, priority: String) {
        val note = NoteSave(
            title = title,
            description = description,
            date = date,
            time = time,
            priority = priority
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                noteDatabase.noteDao().insert(note)
            }
            runOnUiThread {
                Toast.makeText(this@NoteTakingActivity, "Note saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
