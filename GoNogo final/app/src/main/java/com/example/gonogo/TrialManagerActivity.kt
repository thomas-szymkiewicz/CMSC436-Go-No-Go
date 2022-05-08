package com.example.gonogo

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrialManagerActivity : FragmentActivity() {

    private lateinit var mAdapter: TrialListAdapter
    lateinit var mTask: TaskItem

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = TrialListAdapter(this)

        var num =  getIntent().getExtras()!!.getString("num", "0").toInt()
        var date =  getIntent().getExtras()!!.getString("date", "")
        var types =  BooleanArrayListFromString(getIntent().getExtras()!!.getString("types", "[]"))
        var responses =  BooleanArrayListFromString(getIntent().getExtras()!!.getString("responses", "[]"))
        var times =  DoubleArrayListFromString(getIntent().getExtras()!!.getString("times", "[]"))

        mTask = TaskItem(num, date, types, responses, times, false)

        for (i in 0..mTask.types.size-1) {
            mAdapter.add(TrialItem(mTask.types.get(i), mTask.responses.get(i), mTask.times.get(i)))
        }

        mRecyclerView.adapter = mAdapter
    }
}