package com.example.there.convertermvi.domain.usecase.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class SingleUseCase<T, in Params>(
    private val threadScheduler: Scheduler,
    private val postThreadScheduler: Scheduler
) {
    protected abstract fun buildUseCaseSingle(params: Params? = null): Single<T>

    open fun execute(params: Params? = null): Single<T> = buildUseCaseSingle(params)
        .subscribeOn(threadScheduler)
        .observeOn(postThreadScheduler)
}