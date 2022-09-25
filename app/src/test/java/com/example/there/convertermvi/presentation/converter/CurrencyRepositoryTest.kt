package com.example.there.convertermvi.presentation.converter

import com.example.there.convertermvi.data.CurrencyRepository
import com.example.there.convertermvi.data.cache.CurrencyCacheDataStore
import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.data.remote.CurrencyRemoteDataStore
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CurrencyRepositoryTest {
    @Mock
    private lateinit var remoteDataStore: CurrencyRemoteDataStore

    @Mock
    private lateinit var cacheDataStore: CurrencyCacheDataStore

    private lateinit var repository: CurrencyRepository

    private lateinit var testObserver: TestObserver<CurrencyExchangeRates>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = CurrencyRepository(remoteDataStore, cacheDataStore)
        whenever(cacheDataStore.insert(any())).thenReturn(Completable.complete())
    }

    @Test
    fun whenCacheIsEmptyRemoteIsReturned() {
        val returnedByRemote = TestExchangeRates.usdBased
        val singleReturnedByCache = Single.create<CurrencyExchangeRates> { it.onError(Throwable()) }

        whenever(cacheDataStore.get(any())).thenReturn(singleReturnedByCache)
        whenever(remoteDataStore.get(any())).thenReturn(Single.just(returnedByRemote))

        testObserver = repository.get("USD").test()
        testObserver.assertValueAt(0, returnedByRemote)
    }

    @Test
    fun whenCacheIsValidCacheIsReturned() {
        val returnedByCache = TestExchangeRates.usdBased
        val returnedByRemote = TestExchangeRates.eurBased

        whenever(cacheDataStore.get(any())).thenReturn(Single.just(returnedByCache))
        whenever(remoteDataStore.get(any())).thenReturn(Single.just(returnedByRemote))

        testObserver = repository.get("USD").test()
        testObserver.assertValueAt(0, returnedByCache)
    }

    @Test
    fun whenCacheIsInvalidRemoteIsReturned() {
        val returnedByCache = TestExchangeRates.invalid
        val returnedByRemote = TestExchangeRates.eurBased

        whenever(cacheDataStore.get(any())).thenReturn(Single.just(returnedByCache))
        whenever(remoteDataStore.get(any())).thenReturn(Single.just(returnedByRemote))

        testObserver = repository.get("USD").test()
        testObserver.assertValueAt(0, returnedByRemote)
    }
}