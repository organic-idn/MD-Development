package com.example.organic.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.organic.ui.response.*

import com.example.organic.databinding.ActivityMainBinding
import com.example.organic.ui.LiveActivity
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
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.buttonLogout.setOnClickListener{
            logout()
//            val intent = Intent(this, LiveActivity::class.java)
//            startActivity(intent)
        }
        binding.scanSayur.setOnClickListener {
            val intent = Intent(this, LiveActivity::class.java)
            startActivity(intent)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Did not get permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}




