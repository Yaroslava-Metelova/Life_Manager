package com.life_manager.kotlin.life_manager

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.life_manager.kotlin.life_manager.databinding.ActivityMainBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        val someApi = RetrofitHelper.getInstance().create(SomeApi::class.java)

        // запуск нової coroutine
//        MainScope().launch {
//            val result = someApi.getResponseItem().body()?.message
//            binding.textView.text = result.toString()
//        }

        val navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        val navView: BottomNavigationView = binding.bottomNav
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.carsFragment,
                R.id.cryptoFragment,
                R.id.homeFragment,
                R.id.sportsFragment,
                R.id.stocksFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // три крапки справа вгорі
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navigated = NavigationUI.onNavDestinationSelected(item, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    //Без неї теж працює
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment), AppBarConfiguration(
                setOf(
                    R.id.carsFragment,
                    R.id.cryptoFragment,
                    R.id.homeFragment,
                    R.id.sportsFragment,
                    R.id.stocksFragment
                )
            )
        )
    }
}

object RetrofitHelper {

    private const val baseUrl = "https://oyster-app-lxous.ondigitalocean.app/"

    fun getInstance(): Retrofit {
        val client = OkHttpClient().newBuilder().addInterceptor(Interceptor { chain ->
            val request: Request = chain.request()
                .newBuilder()
                .build()
            chain.proceed(request)
        }).build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface SomeApi {
    @GET("/")
    suspend fun getResponseItem(): Response<FirstClass>
}

data class FirstClass(
    val message: String
)
