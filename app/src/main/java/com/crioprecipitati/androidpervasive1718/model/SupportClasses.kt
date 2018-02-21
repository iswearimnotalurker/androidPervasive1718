package com.crioprecipitati.androidpervasive1718.model

import java.sql.Timestamp
import java.util.*

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */
object EmptyTask{
    const val emptyTaskId: Int = -1
    const val emptyTaskOperatorId: Int = -1
    val emptyTaskStatusId: Int = Status.EMPTY.id
    const val emptyTaskActivityId: Int = -1
    val emptyTaskStartTime : Timestamp = Timestamp(Date(0).time )
    val emptyTaskEndTime : Timestamp = Timestamp(Date(1000).time )
    const val emptySessionId = -1
}

object EmptyMember{
    const val emptyMemberId : Int = -2
    const val emptyMemberName: String = "empty member"
}