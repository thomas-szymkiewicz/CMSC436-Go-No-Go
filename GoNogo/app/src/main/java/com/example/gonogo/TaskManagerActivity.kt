package com.example.gonogo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.*
import java.text.ParseException
import java.util.*

class TaskManagerActivity : AppCompatActivity() {

    private lateinit var mAdapter: TaskListAdapter
    private var mTaskCount = 0;

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = TaskListAdapter(this)

        loadItemsFromFile()
        saveItemsToFile()

        /*if (getIntent().getExtras() != null) {
            var types = BooleanArrayListFromString(intent.getStringExtra("types").toString())
                ///BooleanArrayListFromString(getIntent().getExtras()!!.getString("types", "[]"))
            var responses = BooleanArrayListFromString(intent.getStringExtra("responses").toString())
                //BooleanArrayListFromString(getIntent().getExtras()!!.getString("responses", "[]"))
            var times = DoubleArrayListFromString(intent.getStringExtra("times").toString())
                //DoubleArrayListFromString(getIntent().getExtras()!!.getString("times", "[]"))

            if (types.size != 0) {
                var task = TaskItem(mTaskCount+1, Date().toString(), types, responses, times)
                addTask(task)
            }
        }*/

        mRecyclerView.adapter = mAdapter
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        saveItemsToFile()
    }

    fun addTask(task: TaskItem) {
        mTaskCount += 1
        mAdapter.add(task)
        mAdapter.graph()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_DELETE -> {
                mAdapter.clear()
                mTaskCount = 0
                saveItemsToFile()
                mAdapter.graph()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadItemsFromFile() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var num: String?
            var date: String?
            var types: String?
            var responses: String?
            var times: String?
            var mode: String?

            do {
                num = reader.readLine()
                if (num == null) {
                    break
                }
                mTaskCount += 1
                date = reader.readLine()
                types = reader.readLine()
                responses = reader.readLine()
                times = reader.readLine()
                mode = reader.readLine()

                mAdapter.add(TaskItem(mTaskCount, date,
                             BooleanArrayListFromString(types),
                             BooleanArrayListFromString(responses),
                             DoubleArrayListFromString(times),
                    (num.toInt() == 0), mode.toBoolean()))

            }
            while (true)

            mAdapter.doGraph = true

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun saveItemsToFile() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                fos)
                )
            )

            for (idx in 1 until mAdapter.itemCount) {

                writer.println(mAdapter.getItem(idx))

            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    companion object {

        private const val FILE_NAME = "TaskManagerActivityData.txt"
        const val TAG = "gonogo"

        private const val MENU_DELETE = Menu.FIRST
    }
}