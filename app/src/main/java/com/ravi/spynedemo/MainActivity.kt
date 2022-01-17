package com.ravi.spynedemo

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ravi.spynedemo.databinding.ActivityMainBinding
import com.ravi.spynedemo.model.GIFData
import com.ravi.spynedemo.util.NetworkListener
import com.ravi.spynedemo.util.NetworkResult
import com.ravi.spynedemo.util.PaginationScrollListener
import com.ravi.spynedemo.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var networkListener: NetworkListener

    private var pageNum = 1
    var page = -1
    var isLoad = false
    var count = 0

    private val adapter: GifRvAdapter by lazy { GifRvAdapter() }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.inflateMenu(R.menu.menu_search)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // setListeners()
        initRecyclerView()
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(this@MainActivity).collect {
                showNetworkStatus(it)
                readDatabase()
            }
        }
        addGifDataObserver()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readGifs.observeOnce(this@MainActivity, { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    adapter.setData(database.first().gifs.data)
                } else {
                    getGifs()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.menu_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    getSearchVideoCategories(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private fun initRecyclerView() {
        var spanCount = 3
        val orientation = this.resources.configuration.orientation
        spanCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            3
        } else {
            5
        }
        val layoutManager =
            GridLayoutManager(this@MainActivity, spanCount, RecyclerView.VERTICAL, false)
        binding.rvGif.layoutManager = layoutManager
        binding.rvGif.adapter = adapter
        binding.rvGif.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                this@MainActivity.isLoad = true
                pageNum += 1
                if(pageNum<=20){
                    getGifs()
                }

            }

            override val isLastPage: Boolean
                get() = pageNum > 10
            override val isLoading: Boolean
                get() = isLoad

        })
    }

    private fun getGifs() {
        mainViewModel.getGifData(pageNum)


    }
    private fun addGifDataObserver(){
        mainViewModel.gifData.observe(this, { response ->
            response?.let {
                count++


                when (it) {

                    is NetworkResult.Success -> {
                        response.data?.let {
                            isLoad = false
                            showLoader(false)
                            adapter.setData(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        showLoader(false)
                        isLoad = false
                        Toast.makeText(
                            this,
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        showLoader(true)
                        isLoad = true
                    }
                }
            }

        })
        mainViewModel.gifSearchedData.observe(this, { response ->
            response.let {
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            initRecyclerView()
                        }
                    }
                    is NetworkResult.Error -> {
                        showLoader(false)
                        isLoad = false
                        Toast.makeText(
                            this,
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        showLoader(true)
                        isLoad = true
                    }
                }
            }

        })
    }

    private fun getSearchVideoCategories(query: String) {
        mainViewModel.getSearchedGifData(query)

    }

    private fun showLoader(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showNetworkStatus(available: Boolean) {
        if (available) {
            Toast.makeText(application, "We are online.", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(
                application,
                "No Internet Connection. Loading data from local datastore",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}