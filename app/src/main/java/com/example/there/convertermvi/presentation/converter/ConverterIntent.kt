package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.mvi.MviIntent

sealed class ConverterIntent : MviIntent {
    data class InitialIntent(
        val initialBaseCurrency: String,
        val initialChosenCurrency: String
    ) : ConverterIntent()

    data class ReverseCurrenciesIntent(
        val newBaseCurrency: String,
        val newChosenCurrency: String
    ) : ConverterIntent()

    data class ChangeBaseCurrencyIntent(
        val newBaseCurrency: String
    ) : ConverterIntent()

    data class ChangeChosenCurrencyIntent(
        val baseCurrency: String,
        val newChosenCurrency: String
    ) : ConverterIntent()

    data class ChangeBaseCurrencyValueIntent(
        val baseCurrency: String,
        val newValue: Double
    ) : ConverterIntent()
}