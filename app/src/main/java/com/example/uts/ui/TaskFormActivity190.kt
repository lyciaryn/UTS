package com.example.uts.ui

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uts.data.AppDatabase190
import com.example.uts.data.Task190
import com.example.uts.databinding.ActivityTaskForm190Binding
import com.example.uts.util.SessionManager190
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class TaskFormActivity190 : AppCompatActivity() {

    private lateinit var binding190: ActivityTaskForm190Binding
    private lateinit var db190: AppDatabase190
    private lateinit var sessionManager190: SessionManager190

    private var currentTaskId190: Int = -1
    private var isEditMode190: Boolean = false
    private var taskIsDone190: Boolean = false

    companion object {
        const val EXTRA_TASK_ID_190 = "extra_task_id_190"
        const val EXTRA_NAMA_TUGAS_190 = "extra_nama_tugas_190"
        const val EXTRA_PRIORITAS_190 = "extra_prioritas_190"
        const val EXTRA_DEADLINE_190 = "extra_deadline_190"
        const val EXTRA_IS_DONE_190 = "extra_is_done_190"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding190 = ActivityTaskForm190Binding.inflate(layoutInflater)
        setContentView(binding190.root)

        db190 = AppDatabase190.getDatabase190(this)
        sessionManager190 = SessionManager190(this)

        setupPrioritySpinner190()
        setupDatePicker190()

        if (intent.hasExtra(EXTRA_TASK_ID_190)) {
            currentTaskId190 = intent.getIntExtra(EXTRA_TASK_ID_190, -1)
            isEditMode190 = currentTaskId190 != -1
        }

        if (isEditMode190) {
            binding190.tvFormHeaderTitle190.text = "Edit Tugas"
            binding190.btnSaveTask190.text = "Update"
            binding190.btnDeleteTask190.visibility = View.VISIBLE

            val namaTugas190 = intent.getStringExtra(EXTRA_NAMA_TUGAS_190) ?: ""
            val prioritas190 = intent.getStringExtra(EXTRA_PRIORITAS_190) ?: "Tinggi"
            val deadline190 = intent.getStringExtra(EXTRA_DEADLINE_190) ?: ""
            taskIsDone190 = intent.getBooleanExtra(EXTRA_IS_DONE_190, false)

            binding190.etNamaTugas190.setText(namaTugas190)
            binding190.etDeadline190.setText(deadline190)

            val priorities190 = arrayOf("Tinggi", "Sedang", "Rendah")
            val priorityIndex190 = priorities190.indexOf(prioritas190).coerceAtLeast(0)
            binding190.spPrioritas190.setSelection(priorityIndex190)
        } else {
            binding190.tvFormHeaderTitle190.text = "Tambah Tugas Baru"
            binding190.btnSaveTask190.text = "Simpan"
            binding190.btnDeleteTask190.visibility = View.GONE
        }

        binding190.btnSaveTask190.setOnClickListener {
            saveOrUpdateTask190()
        }

        binding190.btnDeleteTask190.setOnClickListener {
            confirmDeleteTask190()
        }

        binding190.btnCancelTask190.setOnClickListener {
            finish()
        }
    }

    private fun setupPrioritySpinner190() {
        val priorities190 = arrayOf("Tinggi", "Sedang", "Rendah")
        val adapter190 = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, priorities190)
        binding190.spPrioritas190.adapter = adapter190
    }

    private fun setupDatePicker190() {
        binding190.etDeadline190.setOnClickListener {
            val calendar190 = Calendar.getInstance()
            val year190 = calendar190.get(Calendar.YEAR)
            val month190 = calendar190.get(Calendar.MONTH)
            val day190 = calendar190.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog190 = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate190 = String.format(
                        Locale.getDefault(),
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                    binding190.etDeadline190.setText(formattedDate190)
                },
                year190,
                month190,
                day190
            )
            datePickerDialog190.show()
        }
    }

    private fun saveOrUpdateTask190() {
        val namaTugas190 = binding190.etNamaTugas190.text.toString().trim()
        val prioritas190 = binding190.spPrioritas190.selectedItem.toString()
        val deadline190 = binding190.etDeadline190.text.toString().trim()
        val userId190 = sessionManager190.getUserId190()

        if (namaTugas190.isEmpty()) {
            binding190.tilNamaTugas190.error = "Nama tugas wajib diisi!"
            return
        } else {
            binding190.tilNamaTugas190.error = null
        }

        if (deadline190.isEmpty()) {
            binding190.tilDeadline190.error = "Deadline wajib dipilih!"
            return
        } else {
            binding190.tilDeadline190.error = null
        }

        if (userId190 == -1) {
            Toast.makeText(this, "Sesi pengguna tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            if (isEditMode190) {
                val updatedTask190 = Task190(
                    id190 = currentTaskId190,
                    userId190 = userId190,
                    namaTugas190 = namaTugas190,
                    prioritas190 = prioritas190,
                    deadline190 = deadline190,
                    isDone190 = taskIsDone190
                )
                withContext(Dispatchers.IO) {
                    db190.taskDao190().updateTask190(updatedTask190)
                }
                Toast.makeText(this@TaskFormActivity190, "Tugas berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            } else {
                val newTask190 = Task190(
                    userId190 = userId190,
                    namaTugas190 = namaTugas190,
                    prioritas190 = prioritas190,
                    deadline190 = deadline190,
                    isDone190 = false
                )
                withContext(Dispatchers.IO) {
                    db190.taskDao190().insertTask190(newTask190)
                }
                Toast.makeText(this@TaskFormActivity190, "Tugas baru berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun confirmDeleteTask190() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Tugas")
            .setMessage("Apakah Anda yakin ingin menghapus tugas ini?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    val taskToDelete190 = Task190(
                        id190 = currentTaskId190,
                        userId190 = sessionManager190.getUserId190(),
                        namaTugas190 = "",
                        prioritas190 = "",
                        deadline190 = ""
                    )
                    withContext(Dispatchers.IO) {
                        db190.taskDao190().deleteTask190(taskToDelete190)
                    }
                    Toast.makeText(this@TaskFormActivity190, "Tugas telah dihapus!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
