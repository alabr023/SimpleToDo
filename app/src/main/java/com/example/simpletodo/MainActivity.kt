package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // remove item from list
                listOfTasks.removeAt(position)
                // notify adapter of change
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

//        findViewById<Button>(R.id.button).setOnClickListener{
//
//            Log.i("Andy", "User clicked on button")
//        }

        loadItems()

        // look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // attach the adapter to the recyclerview to populate item
        recyclerView.adapter = adapter
        // set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // set up the button and input field, so that the user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab user input text @id/addTaskField
            val userInputtedTask: String = inputTextField.text.toString()

            // add the string to list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // notify adapter of updated data
            adapter.notifyItemInserted(listOfTasks.size-1)

            // reset task field
            inputTextField.setText("")

            saveItems()
        }
    }

    // save data when app closes
    // saving by write/read from a file

    // create a method to get data file
    fun getDataFile(): File {

        // each line is task from listoftasks
        return File(filesDir, "data.txt")
    }

    // load items by reading every line from file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // method to save items (write items to file)
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}