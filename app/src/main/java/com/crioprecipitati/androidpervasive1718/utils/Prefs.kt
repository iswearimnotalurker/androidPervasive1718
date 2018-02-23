package com.crioprecipitati.androidpervasive1718.utils

import com.chibatching.kotpref.KotprefModel

/**
 * Created by Famiglia Antonini on 23/02/2018.
 */
object Prefs:KotprefModel(){
    var sessionId by intPref()
}