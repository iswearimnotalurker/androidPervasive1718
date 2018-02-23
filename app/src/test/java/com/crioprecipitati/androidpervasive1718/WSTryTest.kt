package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType
import org.junit.Test
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.Scheduler
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import org.junit.BeforeClass
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class WSTryTest{

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Scheduler.Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
        }
    }


    @Test
    fun WSGetAllSessions(){
        val presenter = LoginPresenterImpl()
        presenter.onConnectRequested(MemberType.LEADER, 0, "non andrà mai")
        Thread(Runnable {
            presenter.onNewSessionRequested("erdf",MemberType.LEADER)
            //presenter.onSessionSelected(MemberType.LEADER, presenter.sessionId)
        }).start()
        Thread.sleep(10000000)
    }
}
