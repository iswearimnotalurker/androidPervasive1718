package com.crioprecipitati.androidpervasive1718.utils

import android.support.design.widget.TextInputEditText

inline fun consumeSessionButton(userCF: String, patientCF: String? = null, callbackForOkParams: () -> Unit) {
    Prefs.userCF = userCF
    patientCF?.let { Prefs.patientCF = patientCF }
    callbackForOkParams()
}

fun TextInputEditText.setTextWithBlankStringCheck(string: String) =
    if (string.isNotBlank()) this.setText(string) else null