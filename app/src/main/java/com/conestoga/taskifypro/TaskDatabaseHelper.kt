package com.conestoga.taskifypro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "TaskDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                description TEXT,
                isCompleted INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)

        // Insert default data
        val tasks = listOf(
            Task(0, "Buy groceries", "Milk, Bread, Eggs", false),
            Task(0, "Meeting with team", "Discuss project milestones", false),
            Task(0, "Workout", "Cardio and Strength", false)
        )
        for (task in tasks) {
            val values = ContentValues().apply {
                put("title", task.title)
                put("description", task.description)
                put("isCompleted", if (task.isCompleted) 1 else 0)
            }
            db.insert("tasks", null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun insertTask(task: Task): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        return db.insert("tasks", null, values) != -1L
    }

    fun updateTask(task: Task): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("isCompleted", if (task.isCompleted) 1 else 0)
        }
        return db.update("tasks", values, "id=?", arrayOf(task.id.toString())) > 0
    }

    fun deleteTask(taskId: Int) {
        val db = writableDatabase
        db.delete("tasks", "id = ?", arrayOf(taskId.toString()))
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val db = readableDatabase
        val taskList = mutableListOf<Task>()
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        while (cursor.moveToNext()) {
            val task = Task(
                id = cursor.getInt(0),
                title = cursor.getString(1),
                description = cursor.getString(2),
                isCompleted = cursor.getInt(3) == 1
            )
            taskList.add(task)
        }
        cursor.close()
        return taskList
    }
}