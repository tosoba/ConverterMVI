package com.example.there.convertermvi.domain.usecase.impl

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.domain.executor.PostExecutionThread
import com.example.there.convertermvi.domain.executor.ThreadExecutor
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import com.example.there.convertermvi.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetCurrencyExchangeRates @Inject constructor(
        private val repository: ICurrencyExchangeRatesRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
): SingleUseCase<CurrencyExchangeRates, String>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Single<CurrencyExchangeRates> = repository.get(params!!)
}