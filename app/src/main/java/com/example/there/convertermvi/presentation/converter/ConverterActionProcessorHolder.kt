package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.domain.usecase.impl.GetCurrencyExchangeRates
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ConverterActionProcessorHolder @Inject constructor(
        private val getCurrencyExchangeRates: GetCurrencyExchangeRates
) {
    private val loadCurrencyExchangeRatesProcessor = ObservableTransformer<
            ConverterAction.LoadCurrencyExchangeRates,
            ConverterResult.LoadCurrencyExchangeRatesResult> {
        it.switchMap {
            getCurrencyExchangeRates.execute(params = it.baseCurrency)
                    .toObservable()
                    .map { ConverterResult.LoadCurrencyExchangeRatesResult.Success(it) }
                    .cast(ConverterResult.LoadCurrencyExchangeRatesResult::class.java)
                    .onErrorReturn { ConverterResult.LoadCurrencyExchangeRatesResult.Failure(it) }
                    .startWith(ConverterResult.LoadCurrencyExchangeRatesResult.InFlight)
        }
    }

    private val changeChosenCurrencyAndRecalculateProcessor = ObservableTransformer<
            ConverterAction.ChangeChosenCurrencyAndRecalculate,
            ConverterResult.ChangeChosenCurrencyAndRecalculateResult> {
        it.switchMap { action ->
            getCurrencyExchangeRates.execute(params = action.baseCurrency)
                    .toObservable()
                    .map {
                        ConverterResult.ChangeChosenCurrencyAndRecalculateResult.Success(
                                exchangeRates = it,
                                newChosenCurrency = action.newChosenCurrency
                        )
                    }
                    .cast(ConverterResult.ChangeChosenCurrencyAndRecalculateResult::class.java)
                    .onErrorReturn { ConverterResult.ChangeChosenCurrencyAndRecalculateResult.Failure(it) }
                    .startWith(ConverterResult.ChangeChosenCurrencyAndRecalculateResult.InFlight)
        }
    }

    private val changeBaseCurrencyValueAndRecalculate = ObservableTransformer<
            ConverterAction.ChangeBaseCurrencyValueAndRecalculate,
            ConverterResult.ChangeBaseCurrencyValueResult> {
        it.switchMap { action ->
            getCurrencyExchangeRates.execute(params = action.baseCurrency)
                    .toObservable()
                    .map {
                        ConverterResult.ChangeBaseCurrencyValueResult.Success(
                                exchangeRates = it,
                                newValue = action.newValue
                        )
                    }
                    .cast(ConverterResult.ChangeBaseCurrencyValueResult::class.java)
                    .onErrorReturn { ConverterResult.ChangeBaseCurrencyValueResult.Failure(it) }
                    .startWith(ConverterResult.ChangeBaseCurrencyValueResult.InFlight)
        }
    }

    val actionProcessor = ObservableTransformer<ConverterAction, ConverterResult> {
        it.publish { shared ->
            Observable.merge(
                    shared.ofType(ConverterAction.LoadCurrencyExchangeRates::class.java)
                            .compose(loadCurrencyExchangeRatesProcessor),
                    shared.ofType(ConverterAction.ChangeChosenCurrencyAndRecalculate::class.java)
                            .compose(changeChosenCurrencyAndRecalculateProcessor),
                    shared.ofType(ConverterAction.ChangeBaseCurrencyValueAndRecalculate::class.java)
                            .compose(changeBaseCurrencyValueAndRecalculate))
                    .mergeWith(
                            shared.filter {
                                it !is ConverterAction.LoadCurrencyExchangeRates
                                        && it !is ConverterAction.ChangeChosenCurrencyAndRecalculate
                                        && it !is ConverterAction.ChangeBaseCurrencyValueAndRecalculate

                            }.flatMap {
                                Observable.error<ConverterResult>(IllegalArgumentException("Unknown Action type: $it"))
                            }
                    )

        }
    }
}