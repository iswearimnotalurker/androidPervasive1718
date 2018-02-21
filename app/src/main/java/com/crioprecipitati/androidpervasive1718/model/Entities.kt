package com.crioprecipitati.androidpervasive1718.model

data class Activity(val id: Int = 0, val name: String, val activityTypeId: Int, val acronym: String, val boundaryId: Int)

data class SessionDNS(val sessionId: Int, val patId: String, val microTaskAddress: String)
