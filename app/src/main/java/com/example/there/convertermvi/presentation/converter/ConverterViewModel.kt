package com.example.there.convertermvi.presentation.converter

import androidx.lifecycle.ViewModel
import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.mvi.MviViewModel
import com.example.there.convertermvi.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val actionProcessorHolder: ConverterActionProcessorHolder
) : ViewModel(), MviViewModel<ConverterIntent, ConverterViewState> {

    private val intentsSubject: PublishSubject<ConverterIntent> = PublishSubject.create()
    private val statesObservable: Observable<ConverterViewState> = compose()

    private val intentFilter: ObservableTransformer<ConverterIntent, ConverterIntent>
        get() = ObservableTransformer {
            it.publish { shared ->
                Observable.merge(
                    shared.ofType(ConverterIntent.InitialIntent::class.java).take(1),
                    shared.notOfType(ConverterIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<ConverterIntent>) =
        intents.subscribe(intentsSubject)

    override fun states(): Observable<ConverterViewState> = statesObservable

    private fun compose(): Observable<ConverterViewState> = intentsSubject.compose(intentFilter)
        .map(this::actionFromIntent)
        .compose(actionProcessorHolder.actionProcessor)
        .scan(ConverterViewState.initial(), reducer)
        .distinctUntilChanged()
        .replay(1)
        .autoConnect(0)

    private fun actionFromIntent(intent: ConverterIntent): ConverterAction = when (intent) {
        is ConverterIntent.InitialIntent ->
            ConverterAction.LoadExchangeRatesAndUpdateChosenCurrency(
                intent.initialBaseCurrency,
                intent.initialChosenCurrency
            )
        is ConverterIntent.ReverseCurrenciesIntent ->
            ConverterAction.LoadExchangeRatesAndUpdateChosenCurrency(
                intent.newBaseCurrency,
                intent.newChosenCurrency
            )
        is ConverterIntent.ChangeBaseCurrencyIntent ->
            ConverterAction.LoadExchangeRates(intent.newBaseCurrency)
        is ConverterIntent.ChangeChosenCurrencyIntent ->
            ConverterAction.ChangeChosenCurrencyAndRecalculate(
                intent.baseCurrency,
                intent.newChosenCurrency
            )
        is ConverterIntent.ChangeBaseCurrencyValueIntent ->
            ConverterAction.ChangeBaseCurrencyValueAndRecalculate(
                intent.baseCurrency,
                intent.newValue
            )
    }

    companion object {
        private val reducer =
            BiFunction { previousState: ConverterViewState, result: ConverterResult ->
                when (result) {
                    is ConverterResult.LoadCurrencyExchangeRatesResult -> {
                        when (result) {
                            is ConverterResult.LoadCurrencyExchangeRatesResult.Success -> previousState.copy(
                                isLoading = false,
                                conversionResult = conversionResult(
                                    previousState.baseCurrencyValue,
                                    previousState.chosenCurrency,
                                    result.exchangeRates
                                ),
                                baseCurrency = result.baseCurrency,
                                descriptionText = descriptionText(
                                    result.baseCurrency,
                                    previousState.chosenCurrency,
                                    result.exchangeRates
                                )
                            )
                            is ConverterResult.LoadCurrencyExchangeRatesResult.Failure -> previousState.toFailure(
                                result.error
                            )
                            is ConverterResult.LoadCurrencyExchangeRatesResult.InFlight -> previousState.toInFlight()
                        }
                    }
                    is ConverterResult.LoadCurrencyExchangeRatesWithChosenCurrencyResult -> {
                        when (result) {
                            is ConverterResult.LoadCurrencyExchangeRatesWithChosenCurrencyResult.Success -> previousState.copy(
                                isLoading = false,
                                conversionResult = conversionResult(
                                    previousState.baseCurrencyValue,
                                    result.chosenCurrency,
                                    result.exchangeRates
                                ),
                                baseCurrency = result.baseCurrency,
                                chosenCurrency = result.chosenCurrency,
                                descriptionText = descriptionText(
                                    result.baseCurrency,
                                    result.chosenCurrency,
                                    result.exchangeRates
                                )
                            )
                            is ConverterResult.LoadCurrencyExchangeRatesWithChosenCurrencyResult.Failure -> previousState.toFailure(
                                result.error
                            )
                            is ConverterResult.LoadCurrencyExchangeRatesWithChosenCurrencyResult.InFlight -> previousState.toInFlight()
                        }
                    }
                    is ConverterResult.ChangeChosenCurrencyAndRecalculateResult -> {
                        when (result) {
                            is ConverterResult.ChangeChosenCurrencyAndRecalculateResult.Success -> previousState.copy(
                                isLoading = false,
                                conversionResult = conversionResult(
                                    previousState.baseCurrencyValue,
                                    result.newChosenCurrency,
                                    result.exchangeRates
                                ),
                                chosenCurrency = result.newChosenCurrency,
                                descriptionText = descriptionText(
                                    previousState.baseCurrency,
                                    result.newChosenCurrency,
                                    result.exchangeRates
                                )
                            )
                            is ConverterResult.ChangeChosenCurrencyAndRecalculateResult.Failure -> previousState.toFailure(
                                result.error
                            )
                            is ConverterResult.ChangeChosenCurrencyAndRecalculateResult.InFlight -> previousState.toInFlight()
                        }
                    }
                    is ConverterResult.ChangeBaseCurrencyValueResult -> {
                        when (result) {
                            is ConverterResult.ChangeBaseCurrencyValueResult.Success -> previousState.copy(
                                isLoading = false,
                                conversionResult = conversionResult(
                                    result.newValue,
                                    previousState.chosenCurrency,
                                    result.exchangeRates
                                ),
                                baseCurrencyValue = result.newValue,
                                descriptionText = descriptionText(
                                    previousState.baseCurrency,
                                    previousState.chosenCurrency,
                                    result.exchangeRates
                                )
                            )
                            is ConverterResult.ChangeBaseCurrencyValueResult.Failure -> previousState.toFailure(
                                result.error
                            )
                            is ConverterResult.ChangeBaseCurrencyValueResult.InFlight -> previousState.toInFlight()
                        }
                    }
                }
            }

        private fun descriptionText(
            baseCurrency: String,
            chosenCurrency: String,
            exchangeRates: CurrencyExchangeRates
        ): String =
            "1 $baseCurrency = ${exchangeRates.rates[chosenCurrency]} $chosenCurrency as of ${exchangeRates.date}"

        private fun conversionResult(
            baseCurrencyValue: Double,
            chosenCurrency: String,
            exchangeRates: CurrencyExchangeRates
        ): String = (baseCurrencyValue * exchangeRates.rates[chosenCurrency]!!).toString()
    }
}