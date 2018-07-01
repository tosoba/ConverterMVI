package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.mvi.MviViewState

data class ConverterViewState(
        val isLoading: Boolean,
        val baseCurrency: String,
        val chosenCurrency: String,
        val baseCurrencyValue: Double,
        val conversionResult: String,
        val descriptionText: String,
        val error: Throwable?
) : MviViewState {

    fun toInFlight(): ConverterViewState = copy(
            isLoading = true,
            conversionResult = "Loading"
    )

    fun toFailure(error: Throwable): ConverterViewState = copy(
            isLoading = false,
            error = error,
            conversionResult = "An error has occurred: ${error.message}"
    )

    companion object {
        fun initial(): ConverterViewState = ConverterViewState(
                isLoading = false,
                baseCurrency = "USD",
                chosenCurrency = "EUR",
                baseCurrencyValue = 0.0,
                conversionResult = "0.0",
                descriptionText = "",
                error = null
        )
    }
}