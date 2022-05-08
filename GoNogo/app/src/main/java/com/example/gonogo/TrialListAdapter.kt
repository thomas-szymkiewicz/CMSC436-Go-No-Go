package com.example.gonogo

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import java.text.NumberFormat

class TrialListAdapter(private val mContext: TrialManagerActivity) :
    RecyclerView.Adapter<TrialListAdapter.ViewHolder>() {

    private var colorToggle = true
    private val mTrials = ArrayList<TrialItem>()

    fun add(item: TrialItem) {
        mTrials.add(item)
        notifyItemChanged(mTrials.size)
    }

    override fun getItemCount(): Int {
        return mTrials.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) HEADER_VIEW_TYPE else TODO_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.task_header_view, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.mBackButton = v.findViewById(R.id.backBtn)
            viewHolder.mTitleView = v.findViewById(R.id.titleView)
            viewHolder.mAverageView = v.findViewById(R.id.averageView)
            viewHolder.mPercentView1 = v.findViewById(R.id.percentView1)
            viewHolder.mPercentView2 = v.findViewById(R.id.percentView2)
            viewHolder.mPercentView3 = v.findViewById(R.id.percentView3)
            viewHolder.mGraphView = v.findViewById(R.id.graph)

            return viewHolder
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.trail_item, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.mItemLayout = v.findViewById(R.id.RelativeLayout1)
            viewHolder.mTypeView = v.findViewById(R.id.typeView)
            viewHolder.mResponseView = v.findViewById(R.id.responseView)
            viewHolder.mTimeView = v.findViewById(R.id.timeView)

            return viewHolder
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {

            viewHolder.mTitleView!!.text = "Task Number " + mContext.mTask.num.toString()
            viewHolder.mAverageView!!.text = ": " + (mContext.mTask.averageResponseTime() * 1000).toInt().toString() + " ms"
            viewHolder.mPercentView1!!.text = "Total Percent Correct: " + (mContext.mTask.totalPercentCorrect() * 100).toInt().toString() + "%"
            viewHolder.mPercentView2!!.text = "Go Percent Correct: " + (mContext.mTask.goPercentCorrect() * 100).toInt().toString() + "%"
            viewHolder.mPercentView3!!.text = "No-Go Percent Correct: " + (mContext.mTask.nogoPercentCorrect() * 100).toInt().toString() + "%"

            var array: Array<DataPoint> = Array(mTrials.size) {DataPoint(0.0, 0.0)}
            var array2: Array<DataPoint> = Array(mContext.mTask.numWrong()) {DataPoint(0.0, 0.0)}

            var j = 0;
            for (i in 0..array.size-1) {
                val trial = mTrials.get(i)
                array[i] = DataPoint((i+1).toDouble(), trial.time * 1000)
                if (!trial.response) {
                    array2[j] = DataPoint((i+1).toDouble(), trial.time * 1000)
                    j += 1
                }
            }

            val series: LineGraphSeries<DataPoint> = LineGraphSeries(array)
            val series2: PointsGraphSeries<DataPoint> = PointsGraphSeries(array2)

            series.color = Color.parseColor(GO_COLOR_1)
            series2.color = Color.parseColor(NOGO_COLOR_1)

            viewHolder.mGraphView!!.addSeries(series)
            viewHolder.mGraphView!!.addSeries(series2)

            val nf: NumberFormat = NumberFormat.getInstance()
            nf.setMaximumFractionDigits(0)

            viewHolder.mGraphView!!.getGridLabelRenderer().setLabelFormatter(DefaultLabelFormatter(nf, nf))

            viewHolder.mGraphView!!.getGridLabelRenderer().setVerticalAxisTitle("Response Time (ms)")
            viewHolder.mGraphView!!.getGridLabelRenderer().setHorizontalAxisTitle("Trial Number")

            viewHolder.mGraphView!!.getViewport().setYAxisBoundsManual(true);
            viewHolder.mGraphView!!.getViewport().setMinY(0.0);
            viewHolder.mGraphView!!.getViewport().setMaxY(1000.0);

            viewHolder.mGraphView!!.getViewport().setXAxisBoundsManual(true);
            viewHolder.mGraphView!!.getViewport().setMinX(0.9);
            viewHolder.mGraphView!!.getViewport().setMaxX(mContext.mTask.types.size.toDouble() + 0.1);

            viewHolder.mBackButton!!.setOnClickListener {
                mContext.finish()
            }
        } else {
            val trialItem = mTrials[position - 1]
            viewHolder.itemView.setOnClickListener {
            }

            viewHolder.mTypeView!!.text = if(trialItem.type) "Go" else "No-Go"
            viewHolder.mResponseView!!.text =if(trialItem.response) "Correct" else "Incorrect"
            viewHolder.mTimeView!!.text = (trialItem.time * 1000).toInt().toString() + " ms"
            if (colorToggle) {
                if (trialItem.response) {
                    viewHolder.mItemLayout!!.setBackgroundColor(Color.parseColor(GO_COLOR_1))
                } else {
                    viewHolder.mItemLayout!!.setBackgroundColor(Color.parseColor(NOGO_COLOR_1))
                }
            } else {
                if (trialItem.response) {
                    viewHolder.mItemLayout!!.setBackgroundColor(Color.parseColor(GO_COLOR_2))
                } else {
                    viewHolder.mItemLayout!!.setBackgroundColor(Color.parseColor(NOGO_COLOR_2))
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

        var mBackButton: Button? = null
        var mTitleView: TextView? = null
        var mAverageView: TextView? = null
        var mPercentView1: TextView? = null
        var mPercentView2: TextView? = null
        var mPercentView3: TextView? = null
        var mGraphView: GraphView? = null

        var mTypeView: TextView? = null
        var mResponseView: TextView? = null
        var mTimeView: TextView? = null
    }

    companion object {
        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.task_header_view
        private const val TODO_VIEW_TYPE = R.layout.task_item
        private const val GO_COLOR_1 = "#0BEC0C"
        private const val GO_COLOR_2 = "#00CE02"
        private const val NOGO_COLOR_1 = "#EC0B0C"
        private const val NOGO_COLOR_2 = "#CE0002"
    }
}