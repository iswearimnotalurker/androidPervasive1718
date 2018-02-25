package com.crioprecipitati.androidpervasive1718.utils

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType

object Prefs:KotprefModel(){
    var sessionId by intPref(-1)
    var instanceId by intPref(0)
    var memberType by enumValuePref(MemberType.LEADER)
    var userCF by stringPref("")
    var patientCF by stringPref("")
}