package com.conestoga.taskifypro

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_theme", false)
        if (isDark) setTheme(android.R.style.ThemeOverlay_Material_Dark)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDatabaseHelper(this)
        taskList = findViewById(R.id.task_recycler)
        taskList.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(
            dbHelper.getAllTasks().toMutableList(),
            onTaskUpdated = { task ->
                dbHelper.updateTask(task)
                loadTasks()
            },
            onTaskDeleted = { task ->
                if (task.id != 0) {
                    dbHelper.deleteTask(task.id)
                    loadTasks()
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid task ID", Toast.LENGTH_SHORT).show()
                }
            }
        )

        taskList.adapter = taskAdapter

        findViewById<Button>(R.id.btn_add).setOnClickListener {
            val title = findViewById<EditText>(R.id.et_title).text.toString().trim()
            val desc = findViewById<EditText>(R.id.et_desc).text.toString().trim()

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.insertTask(Task(0, title, desc, false))
                loadTasks()
                findViewById<EditText>(R.id.et_title).text.clear()
                findViewById<EditText>(R.id.et_desc).text.clear()
            }
        }

        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        loadTasks()
    }

    private fun loadTasks() {
        taskAdapter.updateTasks(dbHelper.getAllTasks())
    }
}
