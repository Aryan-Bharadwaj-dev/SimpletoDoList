package com.example.simpletodolist

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskInput: EditText
    private lateinit var addTaskButton: Button
    private lateinit var taskListView: ListView
    private val tasks = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("ToDoApp", MODE_PRIVATE)
        taskInput = findViewById(R.id.taskInput)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskListView = findViewById(R.id.taskListView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        taskListView.adapter = adapter

        loadTasks() // Load saved tasks when app starts

        addTaskButton.setOnClickListener {
            val task = taskInput.text.toString()
            if (task.isNotEmpty()) {
                tasks.add(task)
                adapter.notifyDataSetChanged()
                taskInput.text.clear()
                saveTasks() // Save tasks
            }
        }

        taskListView.setOnItemClickListener { _, _, position, _ ->
            tasks.removeAt(position)
            adapter.notifyDataSetChanged()
            saveTasks() // Save tasks after deletion
        }
    }

    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("tasks", tasks.toSet())
        editor.apply()
    }

    private fun loadTasks() {
        val savedTasks = sharedPreferences.getStringSet("tasks", emptySet())
        tasks.addAll(savedTasks ?: emptySet())
        adapter.notifyDataSetChanged()
    }
}