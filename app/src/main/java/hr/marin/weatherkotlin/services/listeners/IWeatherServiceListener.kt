package hr.marin.weatherkotlin.services.listeners

interface IWeatherServiceListener<T> {
    fun onSuccess(response:T)
    fun onFailure(t:Throwable)
}