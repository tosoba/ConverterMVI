package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.domain.usecase.impl.GetCurrencyExchangeRates
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ConverterViewModelTest {

    @Mock
    private lateinit var getCurrencyExchangeRates: GetCurrencyExchangeRates

    private lateinit var converterViewModel: ConverterViewModel
    private lateinit var testObserver: TestObserver<ConverterViewState>

    private lateinit var usdBasedCurrencyExchangeRates: CurrencyExchangeRates
    private lateinit var eurBasedCurrencyExchangeRates: CurrencyExchangeRates

    private lateinit var baseCurrencyUSD: String
    private lateinit var baseCurrencyEUR: String
    private lateinit var chosenCurrencyEUR: String
    private lateinit var chosenCurrencyGBP: String

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        baseCurrencyUSD = "USD"
        baseCurrencyEUR = "EUR"
        chosenCurrencyEUR = "EUR"
        chosenCurrencyGBP = "GBP"

        usdBasedCurrencyExchangeRates = TestExchangeRates.usdBased
        eurBasedCurrencyExchangeRates = TestExchangeRates.eurBased

        converterViewModel =
            ConverterViewModel(ConverterActionProcessorHolder(getCurrencyExchangeRates))
        testObserver = converterViewModel.states().test()

        whenever(getCurrencyExchangeRates.execute(baseCurrencyUSD)).thenReturn(
            Single.just(
                usdBasedCurrencyExchangeRates
            )
        )
        whenever(getCurrencyExchangeRates.execute(baseCurrencyEUR)).thenReturn(
            Single.just(
                eurBasedCurrencyExchangeRates
            )
        )
    }

    @Test
    fun viewStateIsInitializedProperly() {
        testObserver.assertValueAt(0, ConverterViewState.initial())
    }

    @Test
    fun initialIntentIsProcessedSuccessfully() {
        converterViewModel.processIntents(
            Observable.just(
                ConverterIntent.InitialIntent(
                    baseCurrencyUSD,
                    chosenCurrencyEUR
                )
            )
        )

        verify(getCurrencyExchangeRates).execute(baseCurrencyUSD)
        testObserver.assertValueAt(1, ConverterViewState::isLoading)
        testObserver.assertValueAt(2) { !it.isLoading }
    }

    @Test
    fun changeBaseCurrencyValueIntentIsProcessedSuccessfully() {
        converterViewModel.processIntents(
            Observable.just(
                ConverterIntent.InitialIntent(baseCurrencyUSD, chosenCurrencyEUR),
                ConverterIntent.ChangeBaseCurrencyValueIntent(baseCurrencyUSD, 5.0)
            )
        )

        verify(getCurrencyExchangeRates, times(2)).execute(baseCurrencyUSD)
        testObserver.assertValueAt(4) {
            !it.isLoading && it.baseCurrency == baseCurrencyUSD
                && it.chosenCurrency == chosenCurrencyEUR && it.baseCurrencyValue == 5.0
                && it.conversionResult == "4.5"
                && it.descriptionText == "1 $baseCurrencyUSD = ${usdBasedCurrencyExchangeRates.rates[chosenCurrencyEUR]} $chosenCurrencyEUR as of ${usdBasedCurrencyExchangeRates.date}"
                && it.error == null
        }
    }

    @Test
    fun changeChosenCurrencyIntentIsProcessedSuccessfully() {
        converterViewModel.processIntents(
            Observable.just(
                ConverterIntent.InitialIntent(baseCurrencyUSD, chosenCurrencyEUR),
                ConverterIntent.ChangeBaseCurrencyValueIntent(baseCurrencyUSD, 5.0),
                ConverterIntent.ChangeChosenCurrencyIntent(baseCurrencyUSD, chosenCurrencyGBP)
            )
        )

        verify(getCurrencyExchangeRates, times(3)).execute(baseCurrencyUSD)
        testObserver.assertValueAt(6) {
            !it.isLoading && it.baseCurrency == baseCurrencyUSD
                && it.chosenCurrency == chosenCurrencyGBP && it.baseCurrencyValue == 5.0
                && it.conversionResult == "4.0"
                && it.descriptionText == "1 $baseCurrencyUSD = ${usdBasedCurrencyExchangeRates.rates[chosenCurrencyGBP]} $chosenCurrencyGBP as of ${usdBasedCurrencyExchangeRates.date}"
                && it.error == null
        }
    }

    @Test
    fun changeBaseCurrencyIntentIsProcessedSuccessfully() {
        converterViewModel.processIntents(
            Observable.just(
                ConverterIntent.InitialIntent(baseCurrencyUSD, chosenCurrencyGBP),
                ConverterIntent.ChangeBaseCurrencyValueIntent(baseCurrencyUSD, 5.0),
                ConverterIntent.ChangeBaseCurrencyIntent(baseCurrencyEUR)
            )
        )

        verify(getCurrencyExchangeRates).execute(baseCurrencyEUR)
        testObserver.assertValueAt(6) {
            !it.isLoading && it.baseCurrency == baseCurrencyEUR
                && it.chosenCurrency == chosenCurrencyGBP && it.baseCurrencyValue == 5.0
                && it.conversionResult == "4.5"
                && it.descriptionText == "1 $baseCurrencyEUR = ${eurBasedCurrencyExchangeRates.rates[chosenCurrencyGBP]} $chosenCurrencyGBP as of ${usdBasedCurrencyExchangeRates.date}"
                && it.error == null
        }
    }

    @Test
    fun reverseCurrenciesIntentIsProcessedSuccessfully() {
        converterViewModel.processIntents(
            Observable.just(
                ConverterIntent.InitialIntent(baseCurrencyUSD, chosenCurrencyEUR),
                ConverterIntent.ChangeBaseCurrencyValueIntent(baseCurrencyUSD, 5.0),
                ConverterIntent.ReverseCurrenciesIntent(
                    newBaseCurrency = chosenCurrencyEUR,
                    newChosenCurrency = baseCurrencyUSD
                )
            )
        )

        verify(getCurrencyExchangeRates).execute(baseCurrencyEUR)
        testObserver.assertValueAt(6) {
            !it.isLoading && it.baseCurrency == baseCurrencyEUR
                && it.chosenCurrency == baseCurrencyUSD && it.baseCurrencyValue == 5.0
                && it.conversionResult == "5.5"
                && it.descriptionText == "1 $baseCurrencyEUR = ${eurBasedCurrencyExchangeRates.rates[baseCurrencyUSD]} $baseCurrencyUSD as of ${eurBasedCurrencyExchangeRates.date}"
                && it.error == null
        }
    }
}