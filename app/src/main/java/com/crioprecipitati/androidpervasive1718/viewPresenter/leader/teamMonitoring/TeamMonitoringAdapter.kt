package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedMember
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.utils.inflate
import com.crioprecipitati.androidpervasive1718.utils.onMemberLongClick
import com.crioprecipitati.androidpervasive1718.utils.onTaskClick
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.android.synthetic.main.item_team_member.view.*

class TeamMonitoringAdapter(private val memberList: List<AugmentedMember>,
                            private val onGroupClickListener: (View, Int, Int) -> Unit,
                            private val onChildClickListener: (View, Int, Int, Int) -> Unit)
    : ExpandableRecyclerViewAdapter<TeamMonitoringAdapter.MemberViewHolder, TeamMonitoringAdapter.TaskViewHolder>(memberList) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(parent.inflate(R.layout.item_team_member))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(parent.inflate(R.layout.item_task))
    }

    override fun onBindGroupViewHolder(holder: MemberViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.onMemberLongClick(flatPosition, onGroupClickListener)
        holder.bind(memberList[flatPosition])
    }

    override fun onBindChildViewHolder(holder: TaskViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        holder.onTaskClick(memberList.indexOfFirst { it.member.userCF == group.title }, childIndex, onChildClickListener)
        holder.bind(childIndex + 1, (group as AugmentedMember).items[childIndex])
    }

    class MemberViewHolder(itemView: View) : GroupViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(itemUser: AugmentedMember) {
            itemView.tvLeaderName.text = "${itemUser.member.name} ${itemUser.member.surname} (${itemUser.items.size} task)"
            with(itemUser.items) {
                when {
                    isEmpty() -> itemView.tvUserFirstTask.text = "---"
                    else -> itemView.tvUserFirstTask.text = first().activityName
                }
            }
        }
    }

    class TaskViewHolder(itemView: View) : ChildViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(ordinal: Int, itemActivity: AugmentedTask) {
            itemView.tvOrdinal.text = "#$ordinal"
            itemView.tvTask.text = itemActivity.activityName
        }
    }

}