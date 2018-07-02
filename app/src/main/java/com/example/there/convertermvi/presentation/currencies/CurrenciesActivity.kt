package com.example.there.convertermvi.presentation.currencies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.there.convertermvi.R
import com.example.there.convertermvi.presentation.Currency
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_currencies.*

class CurrenciesActivity: AppCompatActivity() {

    private val passedCurrency: Currency by lazy { intent.getParcelableExtra<Currency>(EXTRA_CURRENCY) }

    private val currenciesAdapter: CurrenciesAdapter by lazy { CurrenciesAdapter(passedCurrency) }

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)

        initViews()
        bind()
    }

    private fun initViews() {
        currencies_recycler_view?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        currencies_recycler_view?.adapter = currenciesAdapter
    }

    private fun bind() {
        disposables.add(currenciesAdapter.currencyChosen.subscribe {
            val chosenCurrency = passedCurrency.copy(code = it)
            setResultAndFinish(chosenCurrency)
        })
    }

    private fun setResultAndFinish(chosenCurrency: Currency) {
        val resultData = Intent().apply { putExtra(RESULT_NEW_CURRENCY, chosenCurrency) }
        if (chosenCurrency == passedCurrency) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            setResult(Activity.RESULT_OK, resultData)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    companion object {
        private const val EXTRA_CURRENCY = "EXTRA_CURRENCY"

        const val RESULT_NEW_CURRENCY = "RESULT_NEW_CURRENCY"

        const val REQUEST_CODE = 100

        fun start(activity: Activity, currency: Currency) {
            val intent = Intent(activity, CurrenciesActivity::class.java).apply { putExtra(EXTRA_CURRENCY, currency) }
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}