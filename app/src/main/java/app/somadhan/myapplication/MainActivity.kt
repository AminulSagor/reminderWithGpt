package app.somadhan.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.somadhan.myapplication.adapters.NoteAdapter
import app.somadhan.myapplication.database.NoteDatabase
import app.somadhan.myapplication.models.Note
import app.somadhan.myapplication.models.NoteSave
import com.Somadhan.myapplication.R
import com.Somadhan.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBarsInsets.top, 0, systemBarsInsets.bottom)
            insets
        }

        binding.manualButton.setOnClickListener {
            val intent = Intent(this, NoteTakingActivity::class.java)
            startActivity(intent)
        }

        binding.aiButton.setOnClickListener {
            val intent = Intent(this, AiTextActivity::class.java)
            startActivity(intent)
        }

        noteDatabase = NoteDatabase.getDatabase(this) // Ensure you have this method to get database instance

        val recyclerView = findViewById<RecyclerView>(R.id.rvNote)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchNotes()
    }

    private fun fetchNotes() {
        lifecycleScope.launch {
            val notesFromDb = withContext(Dispatchers.IO) {
                noteDatabase.noteDao().getAllNotes()
            }
            val notes = notesFromDb.map { NoteSave ->
                Note(
                    title = NoteSave.title,
                    description = NoteSave.description,
                    date = NoteSave.date
                )
            }
            adapter.updateNotes(notes)
        }
    }
}
