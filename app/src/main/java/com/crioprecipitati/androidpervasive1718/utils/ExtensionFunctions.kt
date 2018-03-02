package com.crioprecipitati.androidpervasive1718.utils

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.model.Member
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

inline fun consumeSessionButton(userCF: String, patientCF: String? = null, callbackForOkParams: () -> Unit) {
    Prefs.userCF = userCF
    patientCF?.let { Prefs.patientCF = patientCF }
    callbackForOkParams()
}

fun TextInputEditText.setTextWithBlankStringCheck(string: String) =
    if (string.isNotBlank()) this.setText(string) else null

fun Member.generateBundle(): Bundle {
    val bundle = Bundle()
    bundle.putString("leaderCF", this.userCF)
    return bundle
}

object Unbudler {
    fun extractMember(bundledMember: Bundle): Member {
        return Member(bundledMember.getString("leaderCF"))
    }
}

fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}

fun <T : RecyclerView.ViewHolder> T.onLongClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnLongClickListener {
        event.invoke(it, adapterPosition, itemViewType)
        true
    }
    return this
}


fun TextView.setHealthParameterValue(value: String) {
    doAsync {
        uiThread {
            this@setHealthParameterValue.text = value
        }
    }
}
