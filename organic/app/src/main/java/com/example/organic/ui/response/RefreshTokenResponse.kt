package com.example.organic.ui.response

data class RefreshTokenResponse(
	var accessToken: String? = null,
	var message: String? = null,
	var status: String? = null
)

data class RefreshTokenRequest(
	var refreshToken: String? = null
)
