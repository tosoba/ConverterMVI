package com.example.there.convertermvi.presentation.converter.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.example.there.convertermvi.R
import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.presentation.converter.ConverterActivity
import com.example.there.convertermvi.presentation.converter.TestConverterApp
import com.example.there.convertermvi.presentation.currencies.CurrenciesAdapter
import io.reactivex.Single
import org.hamcrest.Description
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConverterActivityTest {

    @Rule
    @JvmField
    val converterActivityRule = ActivityTestRule<ConverterActivity>(ConverterActivity::class.java, false, false)

    private lateinit var usdBasedCurrencyExchangeRates: CurrencyExchangeRates
    private lateinit var eurBasedCurrencyExchangeRates: CurrencyExchangeRates

    private lateinit var baseCurrencyUSD: String
    private lateinit var baseCurrencyEUR: String
    private lateinit var chosenCurrencyEUR: String
    private lateinit var chosenCurrencyGBP: String

    @Before
    fun setUp() {
        baseCurrencyUSD = "USD"
        baseCurrencyEUR = "EUR"
        chosenCurrencyEUR = "EUR"
        chosenCurrencyGBP = "GBP"

        usdBasedCurrencyExchangeRates = CurrencyExchangeRates(
                base = "USD",
                date = Date(),
                rates = mapOf("EUR" to 0.9, "GBP" to 0.8, "USD" to 1.0)
        )
        eurBasedCurrencyExchangeRates = CurrencyExchangeRates(
                base = "EUR",
                date = Date(),
                rates = mapOf("USD" to 1.1, "GBP" to 0.9, "EUR" to 1.0)
        )

        `when`(TestConverterApp.appComponent().getCurrencyExchangeRates().execute(baseCurrencyUSD))
                .thenReturn(Single.just(usdBasedCurrencyExchangeRates))
        `when`(TestConverterApp.appComponent().getCurrencyExchangeRates().execute(baseCurrencyEUR))
                .thenReturn(Single.just(eurBasedCurrencyExchangeRates))
    }

    @Test
    fun changeBaseCurrencyValue_correctChosenCurrencyValueIsDisplayed() {
        converterActivityRule.launchActivity(null)

        onView(withId(R.id.base_currency_value_edit_text))
                .perform(typeText("5.0"), closeSoftKeyboard())

        onView(withId(R.id.chosen_currency_value_text_view))
                .check(matches(withText("4.5")))
    }

    @Test
    fun reverseCurrencies_correctChosenCurrencyValueAndCurrencyCodesAreDisplayed() {
        converterActivityRule.launchActivity(null)

        onView(withId(R.id.base_currency_value_edit_text))
                .perform(typeText("5.0"), closeSoftKeyboard())
        onView(withId(R.id.reverse_currencies_button))
                .perform(click())

        onView(withId(R.id.chosen_currency_value_text_view))
                .check(matches(withText("5.5")))
        onView(withId(R.id.base_currency_button))
                .check(matches(withText("EUR")))
        onView(withId(R.id.chosen_currency_button))
                .check(matches(withText("USD")))
    }

    @Test
    fun changeChosenCurrency_correctChosenCurrencyValueAndCurrencyCodesAreDisplayed() {
        val currencyItemMatcher = currenciesRecyclerViewItemWithCode("GBP")

        converterActivityRule.launchActivity(null)

        onView(withId(R.id.base_currency_value_edit_text))
                .perform(typeText("5.0"), closeSoftKeyboard())
        onView(withId(R.id.chosen_currency_button))
                .perform(click())
        onView(withId(R.id.currencies_recycler_view))
                .perform(
                        scrollToHolder(currencyItemMatcher),
                        actionOnHolderItem(currencyItemMatcher, click())
                )

        onView(withId(R.id.chosen_currency_value_text_view))
                .check(matches(withText("4.0")))
        onView(withId(R.id.base_currency_button))
                .check(matches(withText("USD")))
        onView(withId(R.id.chosen_currency_button))
                .check(matches(withText("GBP")))
    }

    @Test
    fun changeBaseCurrency_correctChosenCurrencyValueAndCurrencyCodesAreDisplayed() {
        val currencyItemMatcher = currenciesRecyclerViewItemWithCode("EUR")

        converterActivityRule.launchActivity(null)

        onView(withId(R.id.base_currency_value_edit_text))
                .perform(typeText("5.0"), closeSoftKeyboard())
        onView(withId(R.id.base_currency_button))
                .perform(click())
        onView(withId(R.id.currencies_recycler_view))
                .perform(
                        scrollToHolder(currencyItemMatcher),
                        actionOnHolderItem(currencyItemMatcher, click())
                )

        onView(withId(R.id.chosen_currency_value_text_view))
                .check(matches(withText("5.0")))
        onView(withId(R.id.base_currency_button))
                .check(matches(withText("EUR")))
        onView(withId(R.id.chosen_currency_button))
                .check(matches(withText("EUR")))
    }

    companion object {
        private fun currenciesRecyclerViewItemWithCode(
                code: String
        ) = object : BoundedMatcher<RecyclerView.ViewHolder, CurrenciesAdapter.CurrenciesViewHolder>(
                CurrenciesAdapter.CurrenciesViewHolder::class.java
        ) {
            override fun describeTo(description: Description?) {
                description?.appendText("CurrenciesViewHolder with code: $code")
            }

            override fun matchesSafely(
                    item: CurrenciesAdapter.CurrenciesViewHolder?
            ): Boolean = item?.itemView?.findViewById<TextView>(R.id.currency_item_name_text_view)?.text == code
        }
    }
}