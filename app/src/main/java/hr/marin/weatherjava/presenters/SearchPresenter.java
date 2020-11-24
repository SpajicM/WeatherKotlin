package hr.marin.weatherjava.presenters;

import java.util.List;

import hr.marin.weatherjava.models.SearchResult;
import hr.marin.weatherjava.services.WeatherService;
import hr.marin.weatherjava.services.listeners.IWeatherServiceListener;

public class SearchPresenter {
    private View view;
    private WeatherService service;

    public SearchPresenter(final View view) {
        this.view = view;
        this.service = new WeatherService();
    }

    public void search(String name) {
        service.findLocationByName(name, new IWeatherServiceListener<List<SearchResult>>() {
            @Override
            public void onSuccess(List<SearchResult> searchResults) {
                view.onSearchSuccess(searchResults);
            }

            @Override
            public void onFailure(Throwable t) {
                view.onError();
            }
        });
    }

    public interface View {
        void onSearchSuccess(List<SearchResult> resultList);
        void onError();
    }
}
