package com.example.uts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.example.uts.data.Task190
import com.example.uts.databinding.ItemTask190Binding

class TaskAdapter190(
    private val onItemClick190: (Task190) -> Unit,
    private val onCheckChange190: (Task190, Boolean) -> Unit,
    private val onDeleteClick190: (Task190) -> Unit
) : ListAdapter<Task190, TaskAdapter190.TaskViewHolder190>(TaskDiffCallback190()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder190 {
        val binding190 = ItemTask190Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder190(binding190)
    }

    override fun onBindViewHolder(holder: TaskViewHolder190, position: Int) {
        val task190 = getItem(position)
        holder.bind190(task190)
    }

    inner class TaskViewHolder190(private val binding190: ItemTask190Binding) :
        RecyclerView.ViewHolder(binding190.root) {

        fun bind190(task190: Task190) {
            binding190.tvNamaTugas190.text = task190.namaTugas190
            binding190.tvDeadline190.text = "Deadline: ${task190.deadline190}"
            binding190.tvPriority190.text = task190.prioritas190

            // Priority badge styling
            when (task190.prioritas190) {
                "Tinggi" -> {
                    binding190.tvPriority190.setBackgroundResource(R.drawable.bg_priority_high_190)
                    binding190.tvPriority190.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_high_190)
                    )
                }
                "Sedang" -> {
                    binding190.tvPriority190.setBackgroundResource(R.drawable.bg_priority_medium_190)
                    binding190.tvPriority190.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_medium_190)
                    )
                }
                else -> {
                    binding190.tvPriority190.setBackgroundResource(R.drawable.bg_priority_low_190)
                    binding190.tvPriority190.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.priority_low_190)
                    )
                }
            }

            // Checkbox & strikethrough styling
            binding190.cbTaskDone190.setOnCheckedChangeListener(null)
            binding190.cbTaskDone190.isChecked = task190.isDone190

            binding190.tvNamaTugas190.paint.isStrikeThruText = task190.isDone190
            if (task190.isDone190) {
                binding190.tvNamaTugas190.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.text_secondary_190)
                )
            } else {
                binding190.tvNamaTugas190.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.text_primary_190)
                )
            }

            binding190.cbTaskDone190.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange190(task190, isChecked)
            }

            binding190.btnEdit190.setOnClickListener {
                onItemClick190(task190)
            }

            binding190.btnDelete190.setOnClickListener {
                onDeleteClick190(task190)
            }

            binding190.cardTaskItem190.setOnClickListener {
                onItemClick190(task190)
            }
        }
    }

    class TaskDiffCallback190 : DiffUtil.ItemCallback<Task190>() {
        override fun areItemsTheSame(oldItem: Task190, newItem: Task190): Boolean {
            return oldItem.id190 == newItem.id190
        }

        override fun areContentsTheSame(oldItem: Task190, newItem: Task190): Boolean {
            return oldItem == newItem
        }
    }
}
