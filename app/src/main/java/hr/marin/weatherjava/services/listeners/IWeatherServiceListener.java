package hr.marin.weatherjava.services.listeners;

public interface IWeatherServiceListener<T> {
    void onSuccess(T response);
    void onFailure(Throwable t);
}