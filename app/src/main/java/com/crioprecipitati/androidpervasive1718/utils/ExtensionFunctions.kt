package com.crioprecipitati.androidpervasive1718.utils

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import com.crioprecipitati.androidpervasive1718.model.Member

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