package com.life_manager.kotlin.life_manager

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
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
    private val menuItems = listOf<com.life_manager.kotlin.life_manager.MenuItem>(
        MenuItem(
            label = R.string.cars,
            image = R.drawable.ic_navigation_cars,
            destinationID = R.id.carsFragment
        ),
        MenuItem(
            label = R.string.crypto,
            image = R.drawable.ic_navigation_crypto,
            destinationID = R.id.cryptoFragment
        ),
        MenuItem(
            label = R.string.home,
            image = R.drawable.ic_navigation_home,
            destinationID = R.id.homeFragment
        ),
        MenuItem(
            label = R.string.sports,
            image = R.drawable.ic_navigation_sports,
            destinationID = R.id.sportsFragment
        ),
        MenuItem(
            label = R.string.stocks,
            image = R.drawable.ic_navigation_stocks,
            destinationID = R.id.stocksFragment
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setMenuItems()

        val someApi = RetrofitHelper.getInstance().create(SomeApi::class.java)

        // запуск нової coroutine
//        MainScope().launch {
//            val result = someApi.getResponseItem().body()?.message
//            binding.textView.text = result.toString()
//        }


    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setMenuItems(){
        menuItems.forEachIndexed { index, menuItem ->
            binding.bottomNav.menu.add(R.menu.menu_navigation, menuItem.destinationID, index, menuItem.label)
                .setIcon(menuItem.image)
        }

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()

        if (navController != null) {
            binding.bottomNav.setupWithNavController(navController)
        }


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
                menuItems.map{ it.destinationID}.toMutableSet()
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

data class MenuItem(
    val label: Int,
    val image: Int,
    val destinationID: Int
)

interface SomeApi {
    @GET("/")
    suspend fun getResponseItem(): Response<FirstClass>
}

data class FirstClass(
    val message: String
)
