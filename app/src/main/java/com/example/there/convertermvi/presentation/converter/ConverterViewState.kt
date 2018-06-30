package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.mvi.MviViewState

data class ConverterViewState(
        val isLoading: Boolean,
        val baseCurrency: String,
        val chosenCurrency: String,
        val baseCurrencyValue: Double,
        val descriptionText: String,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun initial(): ConverterViewState = ConverterViewState(
                isLoading = false,
                baseCurrency = "USD",
                chosenCurrency = "EUR",
                baseCurrencyValue = 0.0,
                descriptionText = "",
                error = null
        )
    }
}