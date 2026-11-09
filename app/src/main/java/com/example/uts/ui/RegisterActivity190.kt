package com.example.uts.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uts.data.AppDatabase190
import com.example.uts.data.User190
import com.example.uts.databinding.ActivityRegister190Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity190 : AppCompatActivity() {

    private lateinit var binding190: ActivityRegister190Binding
    private lateinit var db190: AppDatabase190

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding190 = ActivityRegister190Binding.inflate(layoutInflater)
        setContentView(binding190.root)

        db190 = AppDatabase190.getDatabase190(this)

        binding190.btnRegister190.setOnClickListener {
            val username190 = binding190.etRegUsername190.text.toString().trim()
            val password190 = binding190.etRegPassword190.text.toString().trim()
            val confirmPassword190 = binding190.etRegConfirmPassword190.text.toString().trim()

            // Reset errors
            binding190.tilRegUsername190.error = null
            binding190.tilRegPassword190.error = null
            binding190.tilRegConfirmPassword190.error = null

            // Validations
            if (username190.isEmpty()) {
                binding190.tilRegUsername190.error = "Username wajib diisi!"
                return@setOnClickListener
            }

            if (password190.isEmpty()) {
                binding190.tilRegPassword190.error = "Password wajib diisi!"
                return@setOnClickListener
            }

            if (password190.length < 6) {
                binding190.tilRegPassword190.error = "Password minimal 6 karakter!"
                return@setOnClickListener
            }

            if (confirmPassword190 != password190) {
                binding190.tilRegConfirmPassword190.error = "Konfirmasi password tidak cocok!"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser190 = withContext(Dispatchers.IO) {
                    db190.userDao190().getUserByUsername190(username190)
                }
                if (existingUser190 != null) {
                    binding190.tilRegUsername190.error = "Username sudah terdaftar! Gunakan username lain."
                    return@launch
                }

                val newUser190 = User190(username190 = username190, password190 = password190)
                val result190 = withContext(Dispatchers.IO) {
                    db190.userDao190().insertUser190(newUser190)
                }

                if (result190 > 0) {
                    Toast.makeText(
                        this@RegisterActivity190,
                        "Registrasi Berhasil! Silakan login.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity190,
                        "Registrasi gagal! Coba lagi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding190.tvGoToLogin190.setOnClickListener {
            finish()
        }
    }
}
