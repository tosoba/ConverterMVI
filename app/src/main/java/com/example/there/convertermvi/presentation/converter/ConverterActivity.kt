package com.example.there.convertermvi.presentation.converter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.there.convertermvi.R
import dagger.android.AndroidInjection

class ConverterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_converter)
    }
}
