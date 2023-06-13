package com.example.organic.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.organic.databinding.ActivityLoginBinding
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

import com.example.organic.ui.api.service_builder
import com.example.organic.ui.response.LoginResponse
import com.example.organic.ui.userpreference.UserPreferences
import com.example.organic.ui.ViewModelFactory
import com.example.organic.ui.home.HomeActivity
import com.example.organic.ui.response.LoginRequest
import com.example.organic.ui.signup.SignupActivity



private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //session checking
        setupViewModel()

        //go to signup page
        regisAction()

        //login button handling
        binding.loginButton.setOnClickListener{
            login()
        }

        //login using google
        binding.loginGoogleButton.setOnClickListener{
            //...
        }
    }

    //session checking
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[LoginViewModel::class.java]

        viewModel.getSession().observe(this) { session ->
            if (session) {
                val mainIntent = Intent(this, HomeActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }

    //go to signup page
    private fun regisAction(){
        binding.loginSignup.setOnClickListener{
            val mainIntent = Intent(this, SignupActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }

    private fun login(){
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                binding.emailLayout.error = " "
                binding.emailLayout.requestFocus()
            }
            password.isEmpty() -> {
                binding.passwordLayout.error = " "
                binding.passwordLayout.requestFocus()
            }
            else -> {
                val newLoginRequest = LoginRequest()
                newLoginRequest.email = email
                newLoginRequest.password = password

                val client = service_builder.getApiService().loginUser(newLoginRequest)

                client.enqueue(object: Callback<LoginResponse>{
                    override fun onResponse( call: Call<LoginResponse>,  response: Response<LoginResponse>) {
                        val responseBody = response.body()

                        if (response.isSuccessful) {
                            responseBody?.accessToken?.let { viewModel.saveToken(it) }
                            responseBody?.refreshToken?.let { viewModel.saveRefreshToken(it) }
                            viewModel.login()
                            val mainIntent  = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            if (responseBody != null) {
                                Log.d("message", responseBody.message.toString())
                            }
                        }
                    }
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        onToast("${t.message}")
                    }
                })
            }

        }
    }

    private fun onToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}