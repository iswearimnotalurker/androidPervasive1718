package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.utils.inflate
import com.crioprecipitati.androidpervasive1718.utils.onClick
import kotlinx.android.synthetic.main.item_session.view.*

class ActivityListAdapter(private val activityList: List<Activity>,
                          private val onClickListener: (View, Int, Int) -> Unit) : RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val holder = ActivityViewHolder(parent.inflate(R.layout.item_activity))
        holder.onClick(onClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) =
        holder.bind(activityList[position])

    override fun getItemCount(): Int = activityList.size

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(itemActivity: Activity) {
            itemView.tvSessionName.text = "${itemActivity.name}"
        }

    }

}