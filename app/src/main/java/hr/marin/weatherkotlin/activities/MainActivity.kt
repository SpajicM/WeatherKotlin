package hr.marin.weatherkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.marin.weatherkotlin.R
import hr.marin.weatherkotlin.models.SearchResult
import hr.marin.weatherkotlin.presenters.SearchPresenter

class MainActivity: AppCompatActivity(), SearchPresenter.View {
    private val presenter: SearchPresenter = SearchPresenter(this)

    private lateinit var editTextSearch: EditText
    private lateinit var listViewResults: ListView
    private lateinit var adapter: ArrayAdapter<SearchResult>
    private var searchResults = mutableListOf<SearchResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        editTextSearch = findViewById(R.id.editTextSearch)
        listViewResults = findViewById(R.id.listViewResults)
        editTextSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.search(p0.toString())
            }
        })

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, searchResults)

        listViewResults.adapter = adapter

        listViewResults.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra(WeatherActivity.INTENT_CITY_ID, searchResults[position].woeid)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_map) {
            onMapToolbarAction()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onMapToolbarAction() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSearchSuccess(resultList: List<SearchResult>) {
        searchResults.clear()
        searchResults.addAll(resultList)
        adapter.notifyDataSetChanged()
    }

    override fun onError() {
        Toast.makeText(this, R.string.search_failed, Toast.LENGTH_LONG).show()
    }
}