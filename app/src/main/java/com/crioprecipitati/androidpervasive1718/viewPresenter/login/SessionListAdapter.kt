package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.utils.inflate
import com.crioprecipitati.androidpervasive1718.utils.onClick
import kotlinx.android.synthetic.main.item_session.view.*

class SessionListAdapter(private val sessionList: List<SessionDNS>,
                         private val onClickListener: (View, Int, Int) -> Unit) : RecyclerView.Adapter<SessionListAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val holder = SessionViewHolder(parent.inflate(R.layout.item_session))
        holder.onClick(onClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) =
        holder.bind(sessionList[position])

    override fun getItemCount(): Int = sessionList.size

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(itemSession: SessionDNS) {
            itemView.tvSessionName.text = "Sessione n. ${itemSession.sessionId}"
        }

    }

}