package com.crioprecipitati.androidpervasive1718.utils

import com.google.gson.GsonBuilder

/**
 * Created by Edoardo on 21/02/2018.
 */
object GsonInitializer {
    val gson = GsonBuilder().create()
    fun toJson(src: Any?): String = gson.toJson(src)
    fun <T> fromJson(json : String, clazz: Class<T>) : T = gson.fromJson(json, clazz)
}

fun Any?.asJson(): String = GsonInitializer.toJson(this)
fun Any.toJson(): String = GsonInitializer.toJson(this)