package hr.marin.weatherkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.marin.weatherkotlin.R
import hr.marin.weatherkotlin.models.ConsolidatedWeather
import hr.marin.weatherkotlin.utils.API
import hr.marin.weatherkotlin.utils.DateParser

class WeatherItemAdapter(private val weatherList: List<ConsolidatedWeather>) :
    RecyclerView.Adapter<WeatherItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.textDay)
        val stateImageView: ImageView = view.findViewById(R.id.imageState)
        val stateTextView: TextView = view.findViewById(R.id.textState)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_weather, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val weather = weatherList[position]


        viewHolder.dayTextView.text = DateParser.formatWeekday(weather.applicable_date)
        viewHolder.stateTextView.text = weather.weather_state_name
        Picasso.get().load(API.imagesPath + weather.weather_state_abbr + ".png").into(viewHolder.stateImageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = weatherList.size
}
