package com.life_manager.kotlin.life_manager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.life_manager.kotlin.life_manager.databinding.ActivityMainBinding
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        val navView: BottomNavigationView = binding.bottomNav
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.carsFragment, R.id.homeFragment, R.id.cryptoFragment, R.id.sportsFragment , R.id.stocksFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

object RetrofitHelper {

    private const val baseUrl = "https://oyster-app-lxous.ondigitalocean.app/"

    fun getInstance(): Retrofit {
        Log.e("gh", "gson create")
        val client = OkHttpClient().newBuilder().addInterceptor(Interceptor { chain ->
            val request: Request = chain.request()
                .newBuilder()
                .build()
            chain.proceed(request)
        }).build()
        Log.e("gh", "client build")
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
