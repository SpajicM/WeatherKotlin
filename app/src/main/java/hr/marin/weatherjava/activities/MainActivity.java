package hr.marin.weatherjava.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.models.SearchResult;
import hr.marin.weatherjava.presenters.SearchPresenter;

public class MainActivity extends AppCompatActivity implements SearchPresenter.View{

    private SearchPresenter presenter;

    EditText editTextSearch;
    ListView listViewResults;
    List<SearchResult> searchResults;
    ArrayAdapter<SearchResult> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        presenter = new SearchPresenter(this);

        searchResults = new ArrayList<>();
        editTextSearch = findViewById(R.id.editTextSearch);
        listViewResults = findViewById(R.id.listViewResults);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

         adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, searchResults);

        // Assign adapter to ListView
        listViewResults.setAdapter(adapter);

        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSearchSuccess(List<SearchResult> resultList) {
        searchResults.clear();
        if(resultList != null) {
            searchResults.addAll(resultList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.search_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.menu_map) {
            onMapToolbarAction();
        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMapToolbarAction() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}