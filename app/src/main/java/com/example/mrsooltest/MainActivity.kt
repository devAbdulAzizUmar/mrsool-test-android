package com.example.mrsooltest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mrsooltest.adapters.BillionairesAdapter
import com.example.mrsooltest.databinding.ActivityMainBinding
import com.example.mrsooltest.interfaces.BillionaireLIstItemClickHandler
import com.example.mrsooltest.models.Billionaire
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


const val TAG = "MainActivity";

class MainActivity : AppCompatActivity(), BillionaireLIstItemClickHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var billionairesAdapter: BillionairesAdapter

    val limit = 50
    var isLoadingMore = false
    var isScrolling = false
    var isLastPage = false
    var currentPage = 1
    var currentItemsLength = 0


    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleCount = layoutManager.childCount
            val totalCount = layoutManager.itemCount

            val isAtListEnd = firstVisibleItemPosition + visibleCount >= totalCount

            val shouldPaginate = !isLoadingMore && isAtListEnd && !isLastPage

            if(shouldPaginate){
                Log.e(TAG, "onScrolled: loading", )
                currentPage++

                isLoadingMore = true
                getBillionaires(currentPage * limit)


            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initBillionairesRecyclerView()
        binding.rvBillionaires.addOnScrollListener(scrollListener)

        getBillionaires(currentPage * limit)
    }


    private fun initBillionairesRecyclerView() {
        billionairesAdapter = BillionairesAdapter(this)
        binding.rvBillionaires.apply {

            billionairesAdapter.listItemClickHandler = this@MainActivity

            adapter = billionairesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnRefresh -> {
                currentPage = 1
                getBillionaires(currentPage * limit)

            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getBillionaires(limit: Int) {
//
        if(isLoadingMore){
            binding.progressBarLoadingMore.isVisible = true
        }else{
            binding.progressBar.isVisible = true
        }


        RetrofitInstance.billionairesApi.getBillionaires(limit = limit)
            .enqueue(object : Callback<List<Billionaire>> {
                override fun onResponse(
                    call: Call<List<Billionaire>>,
                    response: Response<List<Billionaire>>
                ) {
                    isLoadingMore = false

                    if (response.isSuccessful && response.body() != null) {
                        billionairesAdapter.billionaires = response.body()!!

                        val listJson = Gson().toJson(billionairesAdapter.billionaires)
                        val sharedPrefs = getSharedPreferences("key", Context.MODE_PRIVATE)
                        sharedPrefs.edit().putString("listData", listJson).apply()
                        billionairesAdapter.notifyItemRangeChanged(
                            0, billionairesAdapter
                                .itemCount
                        )
                        binding.progressBar.isVisible = false
                        binding.progressBarLoadingMore.isVisible = false



                        if(currentItemsLength == billionairesAdapter.itemCount){
                            isLastPage = true
                        }

                        currentItemsLength = billionairesAdapter.itemCount

                    } else {

                        setError(true)
                        binding.progressBarLoadingMore.isVisible = false
                    }
                }

                override fun onFailure(call: Call<List<Billionaire>>, t: Throwable) {
                    loadFromSaved()

                    isLoadingMore = false
                    setError(true)
                    binding.progressBar.isVisible = false
                    binding.progressBarLoadingMore.isVisible = false
                }
            })

    }

    private fun loadFromSaved() {
        if (billionairesAdapter.billionaires.isEmpty()) {
            val sharedPres = getSharedPreferences("key", Context.MODE_PRIVATE)
            val listJson = sharedPres.getString("listData", "[]")
            val listType = object : TypeToken<List<Billionaire>>() {}.type
            billionairesAdapter.billionaires = Gson().fromJson(listJson, listType)
            billionairesAdapter.notifyItemRangeChanged(
                0, billionairesAdapter
                    .itemCount
            )
        }

    }

    private fun setError(hasError: Boolean) {
        binding.progressBar.isVisible = false

    }

    override fun onItemClicked(position: Int, billionaire: Billionaire) {

        val intent = Intent(this, DetailsActivity::class.java)
        val gson = Gson()
        val billionaireJson = gson.toJson(billionaire)

        intent.putExtra("billionaire", billionaireJson)
        startActivity(intent)
    }
}