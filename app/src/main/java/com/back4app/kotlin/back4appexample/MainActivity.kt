package com.back4app.kotlin.back4appexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.TextView
import com.back4app.kotlin.back4appexample.databinding.ActivityMainBinding
import com.parse.Parse
import com.parse.ParseObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

//        val parse = Parse.initialize(
//            Parse.Configuration.Builder(this)
//                .applicationId(getString(R.string.back4app_app_id))
//                .clientKey(getString(R.string.back4app_client_key))
//                .server(getString(R.string.back4app_server_url))
//                .build()
//        );
//        Log.d("Auf", "$parse")



        val someApi = RetrofitHelper.getInstance().create(SomeApi::class.java)

        // запуск нової coroutine
        MainScope().launch {
            val result = someApi.getResponseItem()
            binding.textView.text = result.toString()

        }
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

object RetrofitHelper {

    private const val baseUrl = "https://parseapi.back4app.com/"

    fun getInstance(): Retrofit {



        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("client_key", "EAaDWqDAd7Mk6o5CJBqL4PKvmZVjdr1RZgLIi5dz")
                    .addHeader("app_id", "A6DCgXHHCafMOrykD0Ya8RWMMpHI3f8nlk5vL43W")
                    .build()
                chain.proceed(request)
            }.build())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}

interface SomeApi {
    //@Headers("client_key: EAaDWqDAd7Mk6o5CJBqL4PKvmZVjdr1RZgLIi5dz", "application_id: A6DCgXHHCafMOrykD0Ya8RWMMpHI3f8nlk5vL43W",  )
    @GET("/")
    suspend fun getResponseItem(): Response<FirstClass>
}

data class FirstClass(
    val message: String
)
