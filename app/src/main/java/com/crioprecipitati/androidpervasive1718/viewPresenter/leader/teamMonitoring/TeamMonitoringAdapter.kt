package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedMember
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.utils.inflate
import com.crioprecipitati.androidpervasive1718.utils.onLongClick
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.android.synthetic.main.item_team_member.view.*

class TeamMonitoringAdapter(private val memberList: List<AugmentedMember>,
                            private val onGroupClickListener: (View, Int, Int) -> Unit)
    : ExpandableRecyclerViewAdapter<TeamMonitoringAdapter.MemberViewHolder, TeamMonitoringAdapter.TaskViewHolder>(memberList) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val holder = MemberViewHolder(parent.inflate(R.layout.item_team_member))
        holder.onLongClick(onGroupClickListener)
        return holder
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(parent.inflate(R.layout.item_task))
    }

    override fun onBindGroupViewHolder(holder: MemberViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.bind(memberList[flatPosition])
    }

    override fun onBindChildViewHolder(holder: TaskViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        holder.bind((group as AugmentedMember).items[childIndex])
    }

    class MemberViewHolder(itemView: View) : GroupViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(itemUser: AugmentedMember) {
            itemView.tvUserCF.text = "CF Membro ${itemUser.userCF} con n augmentedTask ${itemUser.items.size}"
        }
    }

    class TaskViewHolder(itemView: View) : ChildViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(itemActivity: AugmentedTask) {
            itemView.tvTask.text = "Activity ${itemActivity.activityName}"
        }
    }

}