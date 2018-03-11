package com.crioprecipitati.androidpervasive1718.utils

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.model.Activity
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
    bundle.putString(BundleStrings.leaderCFBundle, this.userCF)
    return bundle
}

fun Activity.generateBundle(): Bundle {
    val bundle = Bundle()
    bundle.putInt(BundleStrings.activityIdBundle,this.id)
    bundle.putString(BundleStrings.activityNameBundle, this.name)
    bundle.putString(BundleStrings.activityAcronymBundle,this.acronym)
    bundle.putInt(BundleStrings.activityTypeIdBundle,this.activityTypeId)
    bundle.putIntArray(BundleStrings.healthParameterIdsBundle, this.healthParameterIds.toIntArray())
    return bundle
}

object Unbundler {
    fun extractMember(bundledMember: Bundle): Member {
        return Member(bundledMember.getString(BundleStrings.leaderCFBundle))
    }

    fun extractActivity(bundleActivity: Bundle): Activity {
        return Activity(bundleActivity.getInt(BundleStrings.activityIdBundle),
                        bundleActivity.getString(BundleStrings.activityNameBundle),
                        bundleActivity.getInt(BundleStrings.activityTypeIdBundle),
                        bundleActivity.getString(BundleStrings.activityAcronymBundle),
                        bundleActivity.getIntArray(BundleStrings.healthParameterIdsBundle).toList())
    }
}

fun ViewGroup.inflate(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : RecyclerView.ViewHolder> T.onClick(flatPosition: Int? = null, event: (view: View, position: Int, type: Int) -> Unit): T {
    this.itemView.setOnClickListener {
        event.invoke(it, flatPosition ?: this@onClick.adapterPosition, this@onClick.itemViewType)
    }
    return this
}

fun <T : RecyclerView.ViewHolder> T.onMemberLongClick(flatPosition: Int? = null, event: (view: View, position: Int, type: Int) -> Unit): T {
    this.itemView.setOnLongClickListener {
        event(it, flatPosition
                ?: this@onMemberLongClick.adapterPosition, this@onMemberLongClick.itemViewType)
        true
    }
    return this
}

fun <T : RecyclerView.ViewHolder> T.onTaskClick(memberFlatPosition: Int, taskPosition: Int, event: (view: View, memberPosition: Int, taskPosition: Int, type: Int) -> Unit): T {
    this.itemView.setOnClickListener { event(it, memberFlatPosition, taskPosition, this@onTaskClick.itemViewType) }
    return this
}


fun TextView.setHealthParameterValue(value: String) {
    doAsync {
        uiThread {
            this@setHealthParameterValue.text = value
        }
    }
}
