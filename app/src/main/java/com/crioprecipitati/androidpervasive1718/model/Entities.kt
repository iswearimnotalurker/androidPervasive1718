package com.crioprecipitati.androidpervasive1718.model

import android.os.Parcelable
import com.crioprecipitati.androidpervasive1718.utils.KlaxonDate
import com.crioprecipitati.androidpervasive1718.utils.KlaxonListLifeParameter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.util.*

data class Activity(val id: Int = 0, val name: String, val activityTypeId: Int, val acronym: String, val healthParameterIds: List<Int>)

data class SessionDNS(val sessionId: Int, val patientCF: String, val instanceId: Int, val leaderCF: String)

data class SessionAssignment(val patientCF: String, val leaderCF: String)

data class Boundary(val id: Int = 0, val healthParameterId: Int, val upperBound: Double, val lowerBound: Double, val lightWarningOffset: Double, val status: String, val itsGood: Boolean, val minAge: Double, val maxAge: Double)

data class Member(val userCF: String) {

    companion object {
        fun emptyMember(): Member = Member(EmptyMember.emptyMemberName)

        fun defaultMember(): Member = Member("Member")
    }
}

data class MemberWithNameSurname(val userCF: String, val name: String, val surname: String)

data class AugmentedMemberFromServer(val member: MemberWithNameSurname, val items: MutableList<AugmentedTask>?)

class AugmentedMember(val member: MemberWithNameSurname, items: MutableList<AugmentedTask> = mutableListOf()) : ExpandableGroup<AugmentedTask>(member.userCF, items) {

    companion object {
        fun emptyMember(): AugmentedMember = AugmentedMember(MemberWithNameSurname("-1", "", ""))

        fun defaultMember(): AugmentedMember =
            AugmentedMember(MemberWithNameSurname("MemberCF", "nome", "cognome"), mutableListOf(AugmentedTask.defaultAugmentedTask(), AugmentedTask.defaultAugmentedTask(), AugmentedTask.defaultAugmentedTask()))
    }
}

@Parcelize
data class Task constructor(var id: Int = 0, val name: String, val sessionId: Int, val operatorCF: String, @KlaxonDate val startTime: Timestamp, @KlaxonDate var endTime: Timestamp? = null, val activityId: Int, var statusId: Int) : Parcelable{
    companion object {
        fun emptyTask(): Task =
                Task(EmptyTask.emptyTaskId,EmptyTask.emptyTaskName, EmptyTask.emptySessionId, EmptyTask.emptyTaskOperatorId, EmptyTask.emptyTaskStartTime, EmptyTask.emptyTaskEndTime, EmptyTask.emptyTaskActivityId, EmptyTask.emptyTaskStatusId)

        fun defaultTask(): Task =
            Task(1, "Task" ,-1, "defaultCF", Timestamp(Date().time), Timestamp(Date().time + 1000), 1, Status.RUNNING.id)
    }
}

@Parcelize
data class AugmentedTask(val task: Task, @KlaxonListLifeParameter val linkedParameters: List<LifeParameters>, val activityName: String) : Parcelable {
    companion object {
        fun emptyAugmentedTask(): AugmentedTask =
                AugmentedTask(Task.emptyTask(), listOf(), "")

        fun defaultAugmentedTask(): AugmentedTask =
                AugmentedTask(Task.defaultTask(), listOf(LifeParameters.END_TIDAL_CARBON_DIOXIDE, LifeParameters.SYSTOLIC_BLOOD_PRESSURE, LifeParameters.HEART_RATE), "Fibroscopia")
    }
}