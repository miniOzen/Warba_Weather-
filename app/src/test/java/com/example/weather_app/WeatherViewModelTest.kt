import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weather_app.Data.Database.Entities.SavedCityEntity
import com.example.weather_app.Data.remote.Model.ForecastWeatherResponse
import com.example.weather_app.Features.CurrentAndForeCastWeather.ForecastIntent
import com.example.weather_app.Features.CurrentAndForeCastWeather.ForecastState
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.CurrentWeatherUIModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.Model.ForeCastUiModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.WeatherViewModel
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.CurrentWeatherUseCase
import com.example.weather_app.Features.CurrentAndForeCastWeather.usecase.ForeCastUseCase
import com.example.weather_app.Features.common.SaveCityUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData runs synchronously

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var saveCityUseCase: SaveCityUseCase
    private lateinit var currentWeatherUseCase: CurrentWeatherUseCase
    private lateinit var foreCastUseCase: ForeCastUseCase
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        saveCityUseCase = mockk()
        currentWeatherUseCase = mockk()
        foreCastUseCase = mockk()
        viewModel = WeatherViewModel(saveCityUseCase, currentWeatherUseCase, foreCastUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCurrentWeather updates LiveData on success`() = runTest {
        val fakeWeather = CurrentWeatherUIModel(
            name = "TestCity",
            feels_like = 25.0,
            speed = 5.0,
            temp_min = 20.0,
            calledTime = 0L,
            temp_max = 28.0,
            temp = 26.0,
            icon = "01d",
            humidity = 60,
            weather = "Clear"
        )

        coEvery { currentWeatherUseCase.invoke() } returns fakeWeather

        val observer = mockk<Observer<CurrentWeatherUIModel>>(relaxed = true)
        viewModel.weather.observeForever(observer)

        viewModel.fetchCurrentWeather()
        advanceUntilIdle()

        coVerify { currentWeatherUseCase.invoke() }
        assertEquals(fakeWeather, viewModel.weather.value)
        viewModel.weather.removeObserver(observer)
    }

    @Test
    fun `fetchCurrentWeather sets error LiveData on exception`() = runTest {
        coEvery { currentWeatherUseCase.invoke() } throws RuntimeException("Network error")

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.error.observeForever(observer)

        viewModel.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals("Network error", viewModel.error.value)
        viewModel.error.removeObserver(observer)
    }

    @Test
    fun `getForeCast updates state on success`() = runTest {
        val fakeForecastResponse = mockk<ForecastWeatherResponse>()
        coEvery { foreCastUseCase.invoke() } returns Result.success(fakeForecastResponse)

        // We mock the toUiModel mapping if needed, or keep empty list for simplicity
        every { fakeForecastResponse.list } returns emptyList()

        val states = mutableListOf<ForecastState>()
        val job = launch { viewModel.state.toList(states) }

        viewModel.processIntent(ForecastIntent.LoadForecast)
        advanceUntilIdle()

        coVerify { foreCastUseCase.invoke() }

        val lastState = states.last()
        assertFalse(lastState.isLoading)
        assertNull(lastState.error)
        assertEquals(0, lastState.forecast?.size)

        job.cancel()
    }

    @Test
    fun `getForeCast sets error state on failure`() = runTest {
        coEvery { foreCastUseCase.invoke() } returns Result.failure(RuntimeException("API error"))

        val states = mutableListOf<ForecastState>()
        val job = launch { viewModel.state.toList(states) }

        viewModel.processIntent(ForecastIntent.LoadForecast)
        advanceUntilIdle()

        val lastState = states.last()
        assertFalse(lastState.isLoading)
        assertEquals("API error", lastState.error)

        job.cancel()
    }

    @Test
    fun `saveCity calls saveCityUseCase`() = runTest {
        val city = SavedCityEntity(0, "TestCity", "KW", 0.0, 0.0)
        coEvery { saveCityUseCase.invoke(city) } just Runs

        viewModel.saveCity(city)
        advanceUntilIdle()

        coVerify { saveCityUseCase.invoke(city) }
    }
}
