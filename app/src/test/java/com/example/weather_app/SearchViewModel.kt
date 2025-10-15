package com.example.weather_app


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.remote.Model.SearchedCityResponse
import com.example.weather_app.Features.SearchCity.SearchViewModel
import com.example.weather_app.Features.SearchCity.SearchCityUseCase
import com.example.weather_app.Features.common.SaveCityUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var saveCityUseCase: SaveCityUseCase
    private lateinit var searchCityUseCase: SearchCityUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        saveCityUseCase = mockk()
        searchCityUseCase = mockk()
        viewModel = SearchViewModel(saveCityUseCase, searchCityUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSearchCity updates LiveData on success`() = runTest {
        val cityName = "Kuwait"
        val fakeResult = listOf(
            SearchedCityResponse(name = "Kuwait City", lat = 29.3759, lon = 47.9774, country = "KW","kuwait")
        )

        coEvery { searchCityUseCase.invoke(cityName) } returns fakeResult

        val observer = mockk<Observer<List<SearchedCityResponse>>>(relaxed = true)
        viewModel.searchResult.observeForever(observer)

        viewModel.getSearchCity(cityName)
        advanceUntilIdle()

        coVerify { searchCityUseCase.invoke(cityName) }
        assertEquals(fakeResult, viewModel.searchResult.value)

        viewModel.searchResult.removeObserver(observer)
    }

    @Test
    fun `getSearchCity sets error LiveData on exception`() = runTest {
        val cityName = "Kuwait"
        coEvery { searchCityUseCase.invoke(cityName) } throws RuntimeException("API error")

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)

        viewModel.getSearchCity(cityName)
        advanceUntilIdle()

        assertEquals("API error", viewModel.error.value)
        viewModel.error.removeObserver(observer)
    }

    @Test
    fun `saveCity calls saveCityUseCase`() = runTest {
        val city = SavedCityEntity(0, "Kuwait City", "KW", 29.3759, 47.9774)
        coEvery { saveCityUseCase.invoke(city) } just Runs

        viewModel.saveCity(city)
        advanceUntilIdle()

        coVerify { saveCityUseCase.invoke(city) }
    }
}
