package com.example.mrsooltest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mrsooltest.adapters.BillionairesAdapter
import com.example.mrsooltest.databinding.ActivityMainBinding
import com.example.mrsooltest.interfaces.BillionaireLIstItemClickHandler
import com.example.mrsooltest.models.Billionaire
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val TAG = "MainActivity"
const val FLUTTER_ENGINE_ID = "flutter_engine_id"
const val METHOD_CHANNEL = "method_channel"

class MainActivity : AppCompatActivity(), BillionaireLIstItemClickHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var billionairesAdapter: BillionairesAdapter

    private lateinit var flutterEngine: FlutterEngine
    private lateinit var flutterMethodChannel: MethodChannel

    val limit = 50
    var isLoadingMore = false
    var isScrolling = false
    var isLastPage = false
    var currentPage = 1
    var currentItemsLength = 0
    private var billionaireJson = ""


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
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

            if (shouldPaginate) {
                Log.e(TAG, "onScrolled: loading")
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
        initFlutterEngine()


    }

    override fun onBackPressed() {

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
        setError(false)
        if (isLoadingMore) {
            binding.progressBarLoadingMore.isVisible = true
        } else {
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



                        if (currentItemsLength == billionairesAdapter.itemCount) {
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

    private fun initFlutterEngine() {

        flutterEngine = FlutterEngine(this)

        flutterMethodChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            METHOD_CHANNEL
        )

        flutterMethodChannel.setMethodCallHandler { call, result ->
            if (call.method == "getBillionaireJson") {
                result.success(billionaireJson)
            }
        }

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )


        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put(FLUTTER_ENGINE_ID, flutterEngine)

    }

    override fun onItemClicked(position: Int, billionaire: Billionaire) {
        val gson = Gson()
        billionaireJson = gson.toJson(billionaire)
        flutterMethodChannel.invokeMethod("refresh", null)


        val flutterIntent = FlutterActivity
            .withCachedEngine(FLUTTER_ENGINE_ID)
            .build(this)

        startActivity(flutterIntent)


//        val intent = Intent(this, DetailsActivity::class.java)

//
//        intent.putExtra("billionaire", billionaireJson)
//        startActivity(intent)
    }
}