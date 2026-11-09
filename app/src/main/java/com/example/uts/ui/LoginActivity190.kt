package com.example.uts.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uts.data.AppDatabase190
import com.example.uts.databinding.ActivityLogin190Binding
import com.example.uts.util.SessionManager190
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity190 : AppCompatActivity() {

    private lateinit var binding190: ActivityLogin190Binding
    private lateinit var sessionManager190: SessionManager190
    private lateinit var db190: AppDatabase190

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager190 = SessionManager190(this)

        // Redirect if already logged in
        if (sessionManager190.isLoggedIn190()) {
            startActivity(Intent(this, DashboardActivity190::class.java))
            finish()
            return
        }

        binding190 = ActivityLogin190Binding.inflate(layoutInflater)
        setContentView(binding190.root)

        db190 = AppDatabase190.getDatabase190(this)

        binding190.btnLogin190.setOnClickListener {
            val username190 = binding190.etUsername190.text.toString().trim()
            val password190 = binding190.etPassword190.text.toString().trim()

            if (username190.isEmpty()) {
                binding190.tilUsername190.error = "Username tidak boleh kosong!"
                return@setOnClickListener
            } else {
                binding190.tilUsername190.error = null
            }

            if (password190.isEmpty()) {
                binding190.tilPassword190.error = "Password tidak boleh kosong!"
                return@setOnClickListener
            } else {
                binding190.tilPassword190.error = null
            }

            lifecycleScope.launch {
                val user190 = withContext(Dispatchers.IO) {
                    db190.userDao190().loginUser190(username190, password190)
                }
                if (user190 != null) {
                    sessionManager190.createSession190(user190.id190, user190.username190)
                    Toast.makeText(this@LoginActivity190, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity190, DashboardActivity190::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity190, "Username atau Password salah!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding190.tvGoToRegister190.setOnClickListener {
            startActivity(Intent(this, RegisterActivity190::class.java))
        }
    }
}
