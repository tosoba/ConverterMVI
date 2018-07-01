package com.example.there.convertermvi.presentation.converter

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.there.convertermvi.R
import com.example.there.convertermvi.di.vm.ViewModelFactory
import com.example.there.convertermvi.mvi.MviView
import com.example.there.convertermvi.util.toDoubleZeroIfEmpty
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_converter.*
import javax.inject.Inject

class ConverterActivity : AppCompatActivity(), MviView<ConverterIntent, ConverterViewState> {

    private val disposables = CompositeDisposable()

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

        bind()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun intents(): Observable<ConverterIntent> = Observable.merge(
            initialIntent,
            reverseCurrenciesIntent,
            changeBaseCurrencyInputValueIntent
    )

    private val initialIntent: Observable<ConverterIntent.InitialIntent>
        get() = Observable.just(ConverterIntent.InitialIntent(base_currency_button.text.toString(), chosen_currency_button.text.toString()))

    private val reverseCurrenciesIntent: Observable<ConverterIntent.ReverseCurrenciesIntent>
        get() = RxView.clicks(reverse_currencies_button).map {
            ConverterIntent.ReverseCurrenciesIntent(chosen_currency_button.text.toString(), base_currency_button.text.toString())
        }

    private val changeBaseCurrencyInputValueIntent: Observable<ConverterIntent.ChangeBaseCurrencyInputValueIntent>
        get() = RxTextView.textChanges(base_currency_value_edit_text).map {
            ConverterIntent.ChangeBaseCurrencyInputValueIntent(base_currency_button.text.toString(), it.toString().toDoubleZeroIfEmpty)
        }

    override fun render(state: ConverterViewState) {
        chosen_currency_value_text_view.text = state.conversionResult
        base_currency_button.text = state.baseCurrency
        chosen_currency_button.text = state.chosenCurrency
        description_text_view.text = state.descriptionText
    }
}
