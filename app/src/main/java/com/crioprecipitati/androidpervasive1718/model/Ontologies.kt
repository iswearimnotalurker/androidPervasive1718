package com.crioprecipitati.androidpervasive1718.model

enum class Status(val id: Int) {
    RUNNING(2),
    SUSPENDED(1),
    MONITORING(5),
    FINISHED(3),
    ELIMINATED(4),
    EMPTY(6)
}

enum class Activites(val id: Int){
    MANEUVERS(3),
    DRUGS(1)
}