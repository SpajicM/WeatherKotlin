package hr.marin.weatherkotlin.presenters


import hr.marin.weatherkotlin.models.SearchResult
import hr.marin.weatherkotlin.services.WeatherService
import hr.marin.weatherkotlin.services.listeners.IWeatherServiceListener

class SearchPresenter(val view: View) {

    private val service: WeatherService = WeatherService()

    fun search(name: String) {
        service.findLocationByName(name, object: IWeatherServiceListener<List<SearchResult>> {
            override fun onSuccess(response: List<SearchResult>) {
                view.onSearchSuccess(response)
            }

            override fun onFailure(t: Throwable) {
                view.onError()
            }
        })
    }

    interface View {
        fun onSearchSuccess(resultList: List<SearchResult>)
        fun onError()
    }
}
