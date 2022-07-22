package com.back4app.kotlin.back4appexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.back4app.kotlin.back4appexample.databinding.ActivityMainBinding
import com.parse.Parse
import com.parse.ParseObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        //setContentView(R.layout.activity_main)

        val someApi = RetrofitHelper.getInstance().create(SomeApi::class.java)

        // запуск нової coroutine
        MainScope().launch {
            val result = someApi.getResponseItem()
            Log.d("ayush: ", result.body()?.count.toString())
            delay(5000)
            binding.result.text = result.body()?.count.toString()
        val textView = findViewById<TextView>(R.id.textView)

//        val firstObject = ParseObject("FirstClass")
//        firstObject.put("message","Hey ! First message from android. Parse is now connected")
//        firstObject.saveInBackground {
//            if (it != null){
//                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
//            }else{
//                Log.d("MainActivity","Object saved.")
//                textView.text = String.format("Object saved. %s", firstObject.get("message"))
//            }
//        }

        textView.text = String.format("Object saved. %s", firstObject.("message"))



        }
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

object RetrofitHelper {

    private const val baseUrl = "https://polyglotte-lycra.ondigitalocean.app"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}

interface SomeApi {
    @GET("/")
    suspend fun getResponseItem(): Response<FirstClass>
}

data class FirstClass(
    val count: Int)
