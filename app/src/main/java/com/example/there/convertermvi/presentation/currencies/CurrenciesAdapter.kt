package com.example.there.convertermvi.presentation.currencies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.convertermvi.R
import com.example.there.convertermvi.presentation.Currency
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrenciesAdapter(
        private val previouslyChosenCurrency: Currency
) : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>() {

    private val currencyChosenSubject = PublishSubject.create<String>()

    val currencyChosen: Observable<String>
        get() = currencyChosenSubject

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrenciesViewHolder = CurrenciesViewHolder(
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.currency_item, parent, false)
    )

    override fun getItemCount(): Int = currencyCodes.size

    override fun onBindViewHolder(holder: CurrenciesViewHolder?, position: Int) {
        val currencyCode = currencyCodes[position]
        holder?.itemView?.let {
            it.currency_item_name_text_view.text = currencyCode
            it.setOnClickListener {
                currencyChosenSubject.onNext(currencyCode)
            }
            if (currencyCode == previouslyChosenCurrency.code) {
                it.currency_item_check_image_view.visibility = View.VISIBLE
            }
        }
    }

    class CurrenciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private val currencyCodes = arrayOf(
                "AUD",
                "BGN",
                "BRL",
                "CAD",
                "CHF",
                "CNY",
                "CZK",
                "DKK",
                "EUR",
                "GBP",
                "HKD",
                "HRK",
                "HUF",
                "IDR",
                "ILS",
                "INR",
                "ISK",
                "JPY",
                "KRW",
                "MXN",
                "MYR",
                "NOK",
                "NZD",
                "PHP",
                "PLN",
                "RON",
                "RUB",
                "SEK",
                "SGD",
                "THB",
                "TRY",
                "USD",
                "ZAR"
        )
    }
}