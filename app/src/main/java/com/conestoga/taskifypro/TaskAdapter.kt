package com.conestoga.taskifypro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskUpdated: (Task) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.task_title)
        val descText: TextView = itemView.findViewById(R.id.task_desc)
        val checkbox: CheckBox = itemView.findViewById(R.id.task_checkbox)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.titleText.text = task.title
        holder.descText.text = task.description
        holder.checkbox.setOnCheckedChangeListener(null) // prevent recycling bug
        holder.checkbox.isChecked = task.isCompleted

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            onTaskUpdated(task)
        }

        holder.deleteBtn.setOnClickListener {
            val removedPosition = holder.adapterPosition
            if (removedPosition != RecyclerView.NO_POSITION) {
                val removedTask = tasks[removedPosition]
                tasks.removeAt(removedPosition)
                notifyItemRemoved(removedPosition)
                onTaskDeleted(removedTask)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
