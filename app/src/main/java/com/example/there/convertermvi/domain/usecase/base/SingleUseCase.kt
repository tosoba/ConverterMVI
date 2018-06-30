package com.example.there.convertermvi.domain.usecase.base

import com.example.there.convertermvi.domain.executor.PostExecutionThread
import com.example.there.convertermvi.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, in Params>(
        private val threadExecutor: ThreadExecutor,
        private val postExecutionThread: PostExecutionThread
) {
    protected abstract fun buildUseCaseObservable(params: Params? = null): Single<T>

    open fun execute(params: Params? = null): Single<T> = buildUseCaseObservable(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
}