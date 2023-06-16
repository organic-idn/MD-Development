package com.example.organic.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.organic.databinding.ActivitySignupBinding
import com.example.organic.ui.api.service_builder
import com.example.organic.ui.response.RegisterResponse
import com.example.organic.ui.login.LoginActivity
import com.example.organic.ui.response.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //go to login page
        loginAction()

        //daftar button handling
        binding.signupButton.setOnClickListener{
            signup()
        }

        //daftar dengan google
        binding.signupGoogleButton.setOnClickListener{
            //..
        }
    }

    //go to login page
    private fun loginAction(){
        binding.signupLogin.setOnClickListener{
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }

    private fun signup(){
        val username = binding.signupUsername.text.toString()
        val email = binding.signupEmail.text.toString().trim()
        val password = binding.signupPassword.text.toString().trim()

        when {
            username.isEmpty() -> {
                binding.usernameLayout.error = " "
                binding.usernameLayout.requestFocus()
            }

            email.isEmpty() -> {
                binding.emailLayout.error = " "
                binding.emailLayout.requestFocus()
            }

            password.isEmpty() -> {
                binding.PasswordLayout.error = " "
                binding.PasswordLayout.requestFocus()
            }
            else -> {
                val newRegisterRequest = RegisterRequest()
                newRegisterRequest.username = username
                newRegisterRequest.email = email
                newRegisterRequest.password = password

                val client = service_builder.getApiService().registerUser(newRegisterRequest)

                client.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        val responseBody = response.body()
                        if (response.isSuccessful) {
                            val mainIntent  = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        } else {
                            if (responseBody != null) {
                                Log.d("message", responseBody.message.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
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