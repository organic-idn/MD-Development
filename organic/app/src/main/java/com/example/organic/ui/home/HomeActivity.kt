package com.example.organic.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.organic.ui.response.*

import com.example.organic.databinding.ActivityMainBinding
import com.example.organic.ui.ViewModelFactory
import com.example.organic.ui.login.LoginActivity
import com.example.organic.ui.userpreference.UserPreferences
import com.example.organic.ui.api.service_builder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogout.setOnClickListener{
            logout()
        }

    }

    private fun logout() {
        var refreshToken = " "
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[HomeViewModel::class.java]
        viewModel.getRefreshToken().observe(this) { session ->
            refreshToken =  session
        }
        val newDeleteRefreshTokenRequest = DeleteRefreshTokenRequest()
        newDeleteRefreshTokenRequest.refreshToken = refreshToken

        val client = service_builder.getApiService().deleteRefreshToken(newDeleteRefreshTokenRequest)

        client.enqueue(object : Callback<DeleteRefreshTokenResponse> {
            override fun onResponse(call: Call<DeleteRefreshTokenResponse>, response: Response<DeleteRefreshTokenResponse>) {
                val responseBody = response.body()

                if (response.isSuccessful) {
                    viewModel.logout()
                    val mainIntent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } else {
                    if (responseBody != null) {
                        Log.d("message", responseBody.message.toString())
                    }
                }
            }

            override fun onFailure(call: Call<DeleteRefreshTokenResponse>, t: Throwable) {
                onToast("${t.message}")
            }
        })
    }

    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}




