package com.example.there.convertermvi.domain.usecase.impl

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import com.example.there.convertermvi.domain.usecase.base.SingleUseCase
import io.reactivex.Scheduler
import io.reactivex.Single

open class GetCurrencyExchangeRates(
        private val repository: ICurrencyExchangeRatesRepository,
        threadScheduler: Scheduler,
        postThreadScheduler: Scheduler
): SingleUseCase<CurrencyExchangeRates, String>(threadScheduler, postThreadScheduler) {

    override fun buildUseCaseObservable(params: String?): Single<CurrencyExchangeRates> = repository.get(params!!)
}