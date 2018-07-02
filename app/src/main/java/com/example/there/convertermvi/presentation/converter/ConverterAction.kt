package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.mvi.MviAction

sealed class ConverterAction : MviAction {
    data class LoadExchangeRates(val baseCurrency: String) : ConverterAction()

    data class LoadExchangeRatesAndUpdateChosenCurrency(
            val baseCurrency: String,
            val chosenCurrency: String
    ) : ConverterAction()

    data class ChangeChosenCurrencyAndRecalculate(
            val baseCurrency: String,
            val newChosenCurrency: String
    ) : ConverterAction()

    data class ChangeBaseCurrencyValueAndRecalculate(
            val baseCurrency: String,
            val newValue: Double
    ) : ConverterAction()
}