package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.mvi.MviResult

sealed class ConverterResult : MviResult {
    sealed class LoadCurrencyExchangeRatesResult : ConverterResult() {
        data class Success(
                val exchangeRates: CurrencyExchangeRates,
                val baseCurrency: String
        ) : LoadCurrencyExchangeRatesResult()
        data class Failure(val error: Throwable) : LoadCurrencyExchangeRatesResult()
        object InFlight : LoadCurrencyExchangeRatesResult()
    }

    sealed class LoadCurrencyExchangeRatesWithChosenCurrencyResult : ConverterResult() {
        data class Success(
                val exchangeRates: CurrencyExchangeRates,
                val baseCurrency: String,
                val chosenCurrency: String
        ) : LoadCurrencyExchangeRatesWithChosenCurrencyResult()
        data class Failure(val error: Throwable) : LoadCurrencyExchangeRatesWithChosenCurrencyResult()
        object InFlight : LoadCurrencyExchangeRatesWithChosenCurrencyResult()
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