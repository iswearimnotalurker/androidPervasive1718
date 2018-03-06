package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations
import model.objectify
import org.junit.Test


class WSTryTest{

//    companion object {
//        @BeforeClass
//        @JvmStatic
//        fun setUpRxSchedulers() {
//            val immediate = object : Scheduler() {
//                override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
//                    // this prevents StackOverflowErrors when scheduling with a delay
//                    return super.scheduleDirect(run, 0, unit)
//                }
//
//                override fun createWorker(): Scheduler.Worker {
//                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
//                }
//            }
//
//            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
//            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
//            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
//            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
//            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
//        }
//    }


    @Test
    fun WSGetAllSessions(){
        val presenter = LoginPresenterImpl()
        presenter.onSessionListRequested()
        Thread(Runnable {
            presenter.onNewSessionRequested()
            //presenter.onSessionSelected(MemberType.LEADER, presenter.sessionId)
        }).start()
        Thread.sleep(10000000)
    }

    @Test
    fun swag() {
        var tmpTask = TaskAssignment(Member.defaultMember(), AugmentedTask.defaultAugmentedTask())

        val wrap = PayloadWrapper(0, WSOperations.ADD_TASK, tmpTask.toJson()).toJson()

        val payload = com.crioprecipitati.androidpervasive1718.utils.GsonInitializer.fromJson(wrap, PayloadWrapper::class.java)

        with(payload) {
            val piadina: TaskAssignment = this.objectify(payload.body)
            println(piadina)
            println(piadina.augmentedTask)
            println(piadina.augmentedTask.linkedParameters)
            println(piadina.augmentedTask.linkedParameters.first().name)
            println(piadina.augmentedTask.linkedParameters.first().javaClass)
            println(piadina.augmentedTask.linkedParameters.javaClass)
        }

    }
}
