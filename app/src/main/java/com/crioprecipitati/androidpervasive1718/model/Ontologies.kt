package com.crioprecipitati.androidpervasive1718.model

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */
enum class Status(val id: Int) {
    RUNNING(2),
    SUSPENDED(1),
    MONITORING(5),
    FINISHED(3),
    ELIMINATED(4),
    EMPTY(6)
}