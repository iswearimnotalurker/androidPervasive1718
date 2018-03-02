package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.inflate
import com.crioprecipitati.androidpervasive1718.utils.onClick
import kotlinx.android.synthetic.main.item_team_member.view.*

class TeamMonitoringAdapter(private val memberList: List<Member>,
                            private val onClickListener: (View, Int, Int) -> Unit) : RecyclerView.Adapter<TeamMonitoringAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val holder = MemberViewHolder(parent.inflate(R.layout.item_team_member))
        holder.onClick(onClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) =
        holder.bind(memberList[position])

    override fun getItemCount(): Int = memberList.size

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(itemUser: Member) {
            itemView.tvUserCF.text = "CF Membro $itemUser"
        }

    }

}