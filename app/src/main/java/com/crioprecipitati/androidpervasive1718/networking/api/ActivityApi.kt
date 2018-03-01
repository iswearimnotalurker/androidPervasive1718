package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.Activity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
interface ActivityApi{
    @GET("activity/all/{activityTypeId}")
    fun getAllActivityByTypeId(@Path ("activityTypeId") activityTypeId: Int) : Observable<List<Activity>>
}
