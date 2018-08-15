package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import java.util.*

object TestExchangeRates {
    val eurBased = CurrencyExchangeRates(
            base = "EUR",
            date = Date(),
            rates = mapOf("USD" to 1.1, "GBP" to 0.9)
    )

    val usdBased = CurrencyExchangeRates(
            base = "USD",
            date = Date(),
            rates = mapOf("EUR" to 0.9, "GBP" to 0.8)
    )

    val invalid = TestExchangeRates.usdBased.copy(date = Date(1999, 5, 12))
}