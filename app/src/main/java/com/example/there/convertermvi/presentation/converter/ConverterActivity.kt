package com.example.there.convertermvi.presentation.converter

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.there.convertermvi.R
import com.example.there.convertermvi.di.vm.ViewModelFactory
import com.example.there.convertermvi.mvi.MviView
import com.example.there.convertermvi.presentation.Currency
import com.example.there.convertermvi.presentation.currencies.CurrenciesActivity
import com.example.there.convertermvi.util.toDoubleZeroIfEmpty
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_converter.*
import javax.inject.Inject

class ConverterActivity : AppCompatActivity(), MviView<ConverterIntent, ConverterViewState> {

    private val disposables = CompositeDisposable()

    private val changeBaseCurrencyPublisher: PublishSubject<ConverterIntent.ChangeBaseCurrencyIntent> =
            PublishSubject.create<ConverterIntent.ChangeBaseCurrencyIntent>()

    private val changeChosenCurrencyPublisher: PublishSubject<ConverterIntent.ChangeChosenCurrencyIntent> =
            PublishSubject.create<ConverterIntent.ChangeChosenCurrencyIntent>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ConverterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
                .of(this, viewModelFactory)
                .get(ConverterViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_converter)

        initViews()
        bind()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    private fun initViews() {
        base_currency_button?.setOnClickListener {
            startCurrenciesActivity(Currency(base_currency_button.text.toString(), Currency.Type.BASE))
        }
        chosen_currency_button.setOnClickListener {
            startCurrenciesActivity(Currency(chosen_currency_button.text.toString(), Currency.Type.CHOSEN))
        }
    }

    private fun startCurrenciesActivity(currency: Currency) = CurrenciesActivity.start(this, currency)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CurrenciesActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val newCurrency = data?.getParcelableExtra<Currency>(CurrenciesActivity.RESULT_NEW_CURRENCY)
                    newCurrency?.let { handleNewCurrency(it) }
                }
            }
        }
    }

    private fun handleNewCurrency(newCurrency: Currency) {
        when (newCurrency.type) {
            Currency.Type.BASE ->
                changeBaseCurrencyPublisher.onNext(ConverterIntent.ChangeBaseCurrencyIntent(newCurrency.code))
            Currency.Type.CHOSEN ->
                changeChosenCurrencyPublisher.onNext(ConverterIntent.ChangeChosenCurrencyIntent(base_currency_button.text.toString(), newCurrency.code))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun intents(): Observable<ConverterIntent> = Observable.merge(
            initialIntent,
            reverseCurrenciesIntent,
            changeBaseCurrencyValueIntent,
            changeBaseCurrencyIntent
    ).mergeWith(changeChosenCurrencyIntent)

    private val initialIntent: Observable<ConverterIntent.InitialIntent>
        get() = Observable.just(ConverterIntent.InitialIntent(base_currency_button.text.toString(), chosen_currency_button.text.toString()))

    private val reverseCurrenciesIntent: Observable<ConverterIntent.ReverseCurrenciesIntent>
        get() = RxView.clicks(reverse_currencies_button).map {
            ConverterIntent.ReverseCurrenciesIntent(chosen_currency_button.text.toString(), base_currency_button.text.toString())
        }

    private val changeBaseCurrencyValueIntent: Observable<ConverterIntent.ChangeBaseCurrencyValueIntent>
        get() = RxTextView.textChanges(base_currency_value_edit_text).map {
            ConverterIntent.ChangeBaseCurrencyValueIntent(base_currency_button.text.toString(), it.toString().toDoubleZeroIfEmpty)
        }

    private val changeBaseCurrencyIntent: Observable<ConverterIntent.ChangeBaseCurrencyIntent>
        get() = changeBaseCurrencyPublisher

    private val changeChosenCurrencyIntent: Observable<ConverterIntent.ChangeChosenCurrencyIntent>
        get() = changeChosenCurrencyPublisher

    override fun render(state: ConverterViewState) {
        chosen_currency_value_text_view.text = state.conversionResult
        base_currency_button.text = state.baseCurrency
        chosen_currency_button.text = state.chosenCurrency
        description_text_view.text = state.descriptionText
    }
}
