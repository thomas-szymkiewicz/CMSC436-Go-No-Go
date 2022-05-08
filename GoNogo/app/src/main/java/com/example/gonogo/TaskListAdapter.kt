package com.example.gonogo

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import java.lang.Double.max
import java.lang.Double.min


class TaskListAdapter(private val mContext: TaskManagerActivity) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    private var colorToggle = true
    private var mTasks = ArrayList<TaskItem>()

    var doGraph = false

    private var mGraph: GraphView? = null

    fun add(item: TaskItem) {
        mTasks.add(item)
        notifyItemChanged(mTasks.size)
    }

    fun clear() {
        mTasks.clear()
        notifyDataSetChanged()
    }

    fun graph() {
        if (mTasks.size != 0) {
            var array: Array<DataPoint> = Array(mTasks.size) { DataPoint(0.0, 0.0) }
            for (i in 0..array.size - 1) {
                val task = mTasks.get(i)
                array[i] =
                    DataPoint(task.averageResponseTime() * 1000, task.totalPercentCorrect() * 100)
            }

            val series: PointsGraphSeries<DataPoint> = PointsGraphSeries(array)
            series.color = Color.parseColor(GRAPH_COLOR)

            mGraph!!.addSeries(series)

            mGraph!!.getViewport().setYAxisBoundsManual(true);
            mGraph!!.getViewport().setMinY(max(minPercent(mTasks) * 100 - 5, -0.1));
            mGraph!!.getViewport().setMaxY(min(maxPercent(mTasks) * 100 + 5, 100.1));

            mGraph!!.getViewport().setXAxisBoundsManual(true);
            mGraph!!.getViewport().setMinX(max(minTime(mTasks) * 1000 - 5, -0.1));
            mGraph!!.getViewport().setMaxX(min(maxTime(mTasks) * 1000 + 5, 1000.1));
        }
    }

    fun getItem(pos: Int): Any {
        return mTasks[pos - 1]
    }

    override fun getItemCount(): Int {
        return mTasks.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) HEADER_VIEW_TYPE else TODO_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.header_view, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.mGraphView = v.findViewById(R.id.graph)
            mGraph = viewHolder.mGraphView

            viewHolder.mGraphView!!.getGridLabelRenderer().setVerticalAxisTitle("Percent Correct")
            viewHolder.mGraphView!!.getGridLabelRenderer().setHorizontalAxisTitle("Average Response Time (ms)")
            if (doGraph) {
                graph()
            }
            viewHolder.mMenuButton = v.findViewById(R.id.menuBtn)

            return viewHolder
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.mItemLayout = v.findViewById(R.id.RelativeLayout1)
            viewHolder.mTitleView = v.findViewById(R.id.titleView)
            viewHolder.mModeView = v.findViewById(R.id.modeView)
            viewHolder.mDateView = v.findViewById(R.id.dateView)

            //graph()

            return viewHolder
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {

            viewHolder.mMenuButton!!.setOnClickListener {
                var intent = Intent(mContext, StartMenu::class.java)
                startActivity(mContext, intent, null);
            }
        } else {
            val taskItem = mTasks[position - 1]
            viewHolder.itemView.setOnClickListener {

                var intent = Intent(mContext, TrialManagerActivity::class.java);
                intent.putExtra("num", taskItem.num.toString());
                intent.putExtra("date", taskItem.date);
                intent.putExtra("types", taskItem.types.toString());
                intent.putExtra("responses", taskItem.responses.toString());
                intent.putExtra("times", taskItem.times.toString());
                intent.putExtra("mode", taskItem.times.toString());
                startActivity(mContext, intent, null);
            }

            viewHolder.mTitleView!!.text = taskItem.num.toString()
            viewHolder.mModeView!!.text = if(taskItem.mode) "Standard" else "Endless"
            viewHolder.mDateView!!.text = taskItem.date

            if (colorToggle) {
                if (taskItem.new) {
                    viewHolder.mItemLayout.setBackgroundColor(Color.parseColor(NEW_COLOR_1))
                } else {
                    viewHolder.mItemLayout.setBackgroundColor(Color.parseColor(COLOR_1))
                }
            } else {
                if (taskItem.new) {
                    viewHolder.mItemLayout.setBackgroundColor(Color.parseColor(NEW_COLOR_2))
                } else {
                    viewHolder.mItemLayout.setBackgroundColor(Color.parseColor(COLOR_2))
                }
            }
            colorToggle = !colorToggle
        }
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong() - 1
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemLayout: View = itemView

        var mMenuButton: Button? = null
        var mGraphView: GraphView? = null

        var mTitleView: TextView? = null
        var mModeView: TextView? = null
        var mDateView: TextView? = null
    }

    companion object {
        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.header_view
        private const val TODO_VIEW_TYPE = R.layout.task_item

        private const val COLOR_1 = "#FFFFFF"
        private const val COLOR_2 = "#DFDFDF"
        private const val NEW_COLOR_1 = "#89D4FF"
        private const val NEW_COLOR_2 = "#54ACDF"
        private const val GRAPH_COLOR = "#6200EE"
    }
}