package com.example.organic.ui.response

data class DeleteRefreshTokenResponse(
	val message: String? = null,
	val status: String? = null
)

data class DeleteRefreshTokenRequest(
	var refreshToken: String? = null
)
