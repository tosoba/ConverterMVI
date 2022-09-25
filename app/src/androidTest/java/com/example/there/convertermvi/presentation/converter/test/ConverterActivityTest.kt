package com.example.there.convertermvi.presentation.converter.test

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
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
    val converterActivityRule =
        ActivityTestRule(ConverterActivity::class.java, false, false)

    private lateinit var usdBasedCurrencyExchangeRate: CurrencyExchangeRates
    private lateinit var eurBasedCurrencyExchangeRate: CurrencyExchangeRates

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

        usdBasedCurrencyExchangeRate = CurrencyExchangeRates(
            base = "USD",
            date = Date(),
            rates = mapOf("EUR" to 0.9, "GBP" to 0.8, "USD" to 1.0)
        )
        eurBasedCurrencyExchangeRate = CurrencyExchangeRates(
            base = "EUR",
            date = Date(),
            rates = mapOf("USD" to 1.1, "GBP" to 0.9, "EUR" to 1.0)
        )

        `when`(TestConverterApp.appComponent().getCurrencyExchangeRates().execute(baseCurrencyUSD))
            .thenReturn(Single.just(usdBasedCurrencyExchangeRate))
        `when`(TestConverterApp.appComponent().getCurrencyExchangeRates().execute(baseCurrencyEUR))
            .thenReturn(Single.just(eurBasedCurrencyExchangeRate))
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
        ) = object :
            BoundedMatcher<RecyclerView.ViewHolder, CurrenciesAdapter.CurrenciesViewHolder>(
                CurrenciesAdapter.CurrenciesViewHolder::class.java
            ) {
            override fun describeTo(description: Description?) {
                description?.appendText("CurrenciesViewHolder with code: $code")
            }

            override fun matchesSafely(
                item: CurrenciesAdapter.CurrenciesViewHolder?
            ): Boolean =
                item?.itemView?.findViewById<TextView>(R.id.currency_item_name_text_view)?.text == code
        }
    }
}