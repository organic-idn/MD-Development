package com.example.organic.ui.response

data class RegisterResponse(
	var message: String? = null,
	var status: String? = null
)

data class RegisterRequest(
	var username: String? = null,
	var email: String? = null,
	var password: String? = null
)
