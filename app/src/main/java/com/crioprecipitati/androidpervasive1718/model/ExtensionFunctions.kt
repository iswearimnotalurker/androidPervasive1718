package com.crioprecipitati.androidpervasive1718.model

import android.os.Bundle

fun Member.generateBundle(): Bundle {
    val bundle = Bundle()
    bundle.putInt("id", this.id)
    bundle.putString("name", this.name)
    return bundle
}

object Unbudler {
    fun extractMember(bundledMember: Bundle): Member {
        return Member(bundledMember.getInt("id"), bundledMember.getString("name"))
    }
}