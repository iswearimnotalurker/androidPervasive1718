package com.crioprecipitati.androidpervasive1718.model

import android.os.Bundle

fun Member.generateBundle(): Bundle {
    val bundle = Bundle()
    bundle.putString("userCF", this.userCF)
    return bundle
}

object Unbudler {
    fun extractMember(bundledMember: Bundle): Member {
        return Member(bundledMember.getString("userCF"))
    }
}