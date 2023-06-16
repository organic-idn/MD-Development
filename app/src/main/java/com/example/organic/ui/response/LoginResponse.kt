package com.example.organic.ui.response

data class LoginResponse(
	var accessToken: String? = null,
	var refreshToken: String? = null,
	var message: String? = null,
	var status: String? = null
)

data class LoginRequest(
	var email: String? = null,
	var password: String? = null
)


