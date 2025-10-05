package com.example.weather_app


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weather_app.Data.Database.SavedCityEntity
import com.example.weather_app.Data.Model.*
import com.example.weather_app.Data.WeatherRepository
import com.example.weather_app.Features.WeatherViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData runs synchronously

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCurrentWeather updates LiveData on success`() = runTest {
        val fakeWeather = CurrentWeatherResponse(
            coord = Coord(0.0,0.0),
            weather = emptyList(),
            base = "stations",
            main = Main(0.0,0.0,0.0,0.0,0,0,null,null),
            visibility = 0,
            wind = Wind(0.0,0,0.0),
            clouds = Clouds(0),
            dt = 0,
            sys = Sys(0,0,"",0,0),
            timezone = 0,
            id = 0,
            name = "TestCity",
            cod = 200
        )

        coEvery { repository.getSavedCities() } returns emptyList()
        coEvery { repository.getCurrentWeather(any()) } returns fakeWeather

        val observer = mockk<Observer<CurrentWeatherResponse>>(relaxed = true)
        viewModel.weather.observeForever(observer)

        viewModel.fetchCurrentWeather()
        advanceUntilIdle()

        coVerify { repository.getCurrentWeather(any()) }
        Assert.assertEquals(fakeWeather, viewModel.weather.value)
        viewModel.weather.removeObserver(observer)
    }

    @Test
    fun `getForeCast updates LiveData on success`() = runTest {
        val fakeForecast = ForecastWeatherResponse(
            "",
            message = 1,
            cnt = 7,
            list = listOf(),
            city = City(
                id = 1,
                name = "",
                coord = Coord(0.0,0.0),
                country = "",
                population = 1,
                timezone = 1,
                sunrise = 1L,
                sunset = 1
            )
        )
        coEvery { repository.getSavedCities() } returns emptyList()
        coEvery { repository.getForecastWeather(any()) } returns fakeForecast

        val observer = mockk<Observer<ForecastWeatherResponse>>(relaxed = true)
        viewModel.forecast.observeForever(observer)

        viewModel.getForeCast()
        advanceUntilIdle()

        coVerify { repository.getForecastWeather(any()) }
        Assert.assertEquals(fakeForecast, viewModel.forecast.value)
        viewModel.forecast.removeObserver(observer)
    }

    @Test
    fun `getSearchCity updates LiveData on success`() = runTest {
        val fakeResult = listOf(SearchedCityResponse("Kuwait", 29.37, 47.97, "KW", null))
        coEvery { repository.getSearchedCity(any()) } returns fakeResult

        val observer = mockk<Observer<List<SearchedCityResponse>>>(relaxed = true)
        viewModel.searchResult.observeForever(observer)

        viewModel.getSearchCity("Kuwait")
        advanceUntilIdle()

        coVerify { repository.getSearchedCity(any()) }
        Assert.assertEquals(fakeResult, viewModel.searchResult.value)
        viewModel.searchResult.removeObserver(observer)
    }

    @Test
    fun `saveCity calls repository saveCity`() = runTest {
        val city = SavedCityEntity(0, "TestCity", "KW",0.0, 0.0, )
        coEvery { repository.saveCity(city) } just Runs

        viewModel.saveCity(city)
        advanceUntilIdle()

        coVerify { repository.saveCity(city) }
    }

    @Test
    fun `fetchCurrentWeather sets error LiveData on exception`() = runTest {
        coEvery { repository.getSavedCities() } returns emptyList()
        coEvery { repository.getCurrentWeather(any()) } throws RuntimeException("Network error")

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)

        viewModel.fetchCurrentWeather()
        advanceUntilIdle()

        Assert.assertEquals("Network error", viewModel.error.value)
        viewModel.error.removeObserver(observer)
    }
}
