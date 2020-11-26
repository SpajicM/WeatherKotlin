package hr.marin.weatherjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.models.ConsolidatedWeather;
import hr.marin.weatherjava.utils.API;
import hr.marin.weatherjava.utils.DateParser;

public class WeatherItemAdapter extends RecyclerView.Adapter<WeatherItemAdapter.ViewHolder> {

    private List<ConsolidatedWeather> weatherList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        ImageView stateImageView;
        TextView stateTextView;

        public ViewHolder(View view) {
            super(view);

            dayTextView = (TextView) view.findViewById(R.id.textDay);
            stateImageView = (ImageView) view.findViewById(R.id.imageState);
            stateTextView = (TextView) view.findViewById(R.id.textState);
        }

        public TextView getDayTextView() {
            return dayTextView;
        }

        public ImageView getStateImageView() {
            return stateImageView;
        }

        public TextView getStateTextView() {
            return stateTextView;
        }
    }

    public WeatherItemAdapter(List<ConsolidatedWeather> dataSet) {
        weatherList = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_weather, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ConsolidatedWeather weather = weatherList.get(position);

        viewHolder.getDayTextView().setText(DateParser.formatWeekday(weather.getApplicableDate()));
        viewHolder.getStateTextView().setText(weather.getWeatherStateName());
        Picasso.get().load(API.getImagesPath() + weather.getWeatherStateAbbr() + ".png").into(viewHolder.getStateImageView());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}

