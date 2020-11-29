package hr.marin.weatherkotlin.presenters

import hr.marin.weatherkotlin.models.Location
import hr.marin.weatherkotlin.services.WeatherService
import hr.marin.weatherkotlin.services.listeners.IWeatherServiceListener

class WeatherPresenter(val view: View) {

    private val service: WeatherService = WeatherService()

    fun getLocationWeather(id: Int) {
        view.showLoader()
        service.getLocationWeather(id, object: IWeatherServiceListener<Location> {
            override fun onSuccess(response: Location) {
                view.onFetchSuccess(response)
                view.hideLoader()
            }

            override fun onFailure(t: Throwable) {
                view.onError()
                view.hideLoader()
            }
        })
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun onFetchSuccess(location: Location)
        fun onError()
    }
}
