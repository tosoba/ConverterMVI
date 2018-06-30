package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.mvi.MviResult

sealed class ConverterResult : MviResult {
    sealed class LoadCurrencyExchangeRatesResult : ConverterResult() {
        data class Success(val exchangeRates: CurrencyExchangeRates) : LoadCurrencyExchangeRatesResult()
        data class Failure(val error: Throwable) : LoadCurrencyExchangeRatesResult()
        object InFlight : LoadCurrencyExchangeRatesResult()
    }

    sealed class ChangeChosenCurrencyAndRecalculateResult : ConverterResult() {
        data class Success(
                val exchangeRates: CurrencyExchangeRates,
                val newChosenCurrency: String
        ) : ChangeChosenCurrencyAndRecalculateResult()

        data class Failure(val error: Throwable) : ChangeChosenCurrencyAndRecalculateResult()
        object InFlight : ChangeChosenCurrencyAndRecalculateResult()
    }

    sealed class ChangeBaseCurrencyValueResult : ConverterResult() {
        data class Success(
                val exchangeRates: CurrencyExchangeRates,
                val newValue: Double
        ) : ChangeBaseCurrencyValueResult()

        data class Failure(val error: Throwable) : ChangeBaseCurrencyValueResult()
        object InFlight : ChangeBaseCurrencyValueResult()
    }
}