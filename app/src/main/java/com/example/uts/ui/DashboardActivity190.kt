package com.example.uts.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts.adapter.TaskAdapter190
import com.example.uts.data.AppDatabase190
import com.example.uts.data.Task190
import com.example.uts.databinding.ActivityDashboard190Binding
import com.example.uts.util.SessionManager190
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity190 : AppCompatActivity() {

    private lateinit var binding190: ActivityDashboard190Binding
    private lateinit var db190: AppDatabase190
    private lateinit var sessionManager190: SessionManager190
    private lateinit var taskAdapter190: TaskAdapter190

    private var currentUserId190: Int = -1
    private var isSortByPriority190: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager190 = SessionManager190(this)

        if (!sessionManager190.isLoggedIn190()) {
            startActivity(Intent(this, LoginActivity190::class.java))
            finish()
            return
        }

        binding190 = ActivityDashboard190Binding.inflate(layoutInflater)
        setContentView(binding190.root)

        db190 = AppDatabase190.getDatabase190(this)
        currentUserId190 = sessionManager190.getUserId190()

        setupHeader190()
        setupRecyclerView190()
        setupListeners190()
        loadTasks190()
    }

    override fun onResume() {
        super.onResume()
        loadTasks190()
    }

    private fun setupHeader190() {
        val username190 = sessionManager190.getUsername190()
        binding190.tvWelcomeUser190.text = "Halo, $username190!"

        binding190.btnLogout190.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya") { _, _ ->
                    sessionManager190.logoutUser190()
                    Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity190::class.java))
                    finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun setupRecyclerView190() {
        taskAdapter190 = TaskAdapter190(
            onItemClick190 = { task190 ->
                val intent190 = Intent(this, TaskFormActivity190::class.java).apply {
                    putExtra(TaskFormActivity190.EXTRA_TASK_ID_190, task190.id190)
                    putExtra(TaskFormActivity190.EXTRA_NAMA_TUGAS_190, task190.namaTugas190)
                    putExtra(TaskFormActivity190.EXTRA_PRIORITAS_190, task190.prioritas190)
                    putExtra(TaskFormActivity190.EXTRA_DEADLINE_190, task190.deadline190)
                    putExtra(TaskFormActivity190.EXTRA_IS_DONE_190, task190.isDone190)
                }
                startActivity(intent190)
            },
            onCheckChange190 = { task190, isDone190 ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db190.taskDao190().updateTaskStatus190(task190.id190, isDone190)
                    }
                    loadTasks190()
                }
            },
            onDeleteClick190 = { task190 ->
                AlertDialog.Builder(this)
                    .setTitle("Hapus Tugas")
                    .setMessage("Hapus '${task190.namaTugas190}'?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                db190.taskDao190().deleteTask190(task190)
                            }
                            Toast.makeText(this@DashboardActivity190, "Tugas dihapus!", Toast.LENGTH_SHORT).show()
                            loadTasks190()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )

        binding190.rvTaskList190.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity190)
            adapter = taskAdapter190
        }
    }

    private fun setupListeners190() {
        binding190.fabAddTask190.setOnClickListener {
            startActivity(Intent(this, TaskFormActivity190::class.java))
        }

        binding190.btnSortPriority190.setOnClickListener {
            isSortByPriority190 = !isSortByPriority190
            binding190.btnSortPriority190.text = if (isSortByPriority190) "Sort: Prioritas" else "Sort: Default"
            loadTasks190()
        }

        binding190.etSearchTask190.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadTasks190(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadTasks190(query190: String = binding190.etSearchTask190.text.toString().trim()) {
        if (currentUserId190 == -1) return

        lifecycleScope.launch {
            val tasksList190: List<Task190> = withContext(Dispatchers.IO) {
                when {
                    query190.isNotEmpty() -> db190.taskDao190().searchTasks190(currentUserId190, query190)
                    isSortByPriority190 -> db190.taskDao190().getTasksSortedByPriority190(currentUserId190)
                    else -> db190.taskDao190().getTasksByUserId190(currentUserId190)
                }
            }

            taskAdapter190.submitList(tasksList190)

            // Update empty state
            if (tasksList190.isEmpty()) {
                binding190.tvEmptyState190.visibility = View.VISIBLE
                binding190.rvTaskList190.visibility = View.GONE
            } else {
                binding190.tvEmptyState190.visibility = View.GONE
                binding190.rvTaskList190.visibility = View.VISIBLE
            }

            // Update Statistics
            val allUserTasks190 = withContext(Dispatchers.IO) {
                db190.taskDao190().getTasksByUserId190(currentUserId190)
            }
            val total190 = allUserTasks190.size
            val completed190 = allUserTasks190.count { it.isDone190 }
            val pending190 = total190 - completed190
            val highPriority190 = allUserTasks190.count { it.prioritas190 == "Tinggi" && !it.isDone190 }

            binding190.tvTotalTasks190.text = total190.toString()
            binding190.tvCompletedTasks190.text = completed190.toString()
            binding190.tvPendingTasks190.text = pending190.toString()
            binding190.tvHighPriorityTasks190.text = highPriority190.toString()
        }
    }
}
