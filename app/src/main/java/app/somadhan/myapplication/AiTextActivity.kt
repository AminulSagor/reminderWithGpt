package app.somadhan.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.Somadhan.myapplication.R
import com.Somadhan.myapplication.databinding.ActivityAiTextBinding

class AiTextActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAiTextBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val keyboardUtil = KeyboardUtil(this, binding.root)
        keyboardUtil.onKeyboardVisibilityChanged = { keyboardVisible ->
            adjustLayoutForKeyboard(keyboardVisible)
        }
        // Add TextWatcher to EditText to change ImageButton icon
        binding.editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.buttonSend.setImageResource(android.R.drawable.ic_btn_speak_now)
                } else {
                    binding.buttonSend.setImageResource(R.drawable.up_arrow)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
    }

    private fun adjustLayoutForKeyboard(keyboardVisible: Boolean) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
        if (keyboardVisible) {
            layoutParams.bottomToTop = R.id.editTextMessage
        } else {
            layoutParams.bottomToTop = ConstraintLayout.LayoutParams.UNSET
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }
        recyclerView.layoutParams = layoutParams
    }


}