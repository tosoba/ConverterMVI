package com.example.there.convertermvi.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.there.convertermvi.data.cache.db.Tables
import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import io.reactivex.Single

@Dao
abstract class CurrencyExchangeRatesDao {
    @Query("DELETE FROM ${Tables.CURRENCY_EXCHANGE_RATES_TABLE}")
    abstract fun clearAll()

    @Query("SELECT * FROM ${Tables.CURRENCY_EXCHANGE_RATES_TABLE} WHERE base = :base")
    abstract fun get(base: String): Single<CurrencyExchangeRates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(cer: CurrencyExchangeRates)
}