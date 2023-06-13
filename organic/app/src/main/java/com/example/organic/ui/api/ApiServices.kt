package com.example.organic.ui.api

import com.example.organic.ui.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    //REGISTERING
    @POST("createUser")
    fun registerUser(
        @Body newRegisterRequest: RegisterRequest
    ): Call<RegisterResponse>

    //LOGIN
    @POST("login")
    fun loginUser(
        @Body newLoginRequest: LoginRequest
    ): Call<LoginResponse>

    //TO GET THE NEW ACCESS TOKEN
    @POST("refreshAccessToken")
    fun getRefreshedToken(
        @Body newRefreshTokenRequest: RefreshTokenRequest
    ): Call<RefreshTokenResponse>

    //TO DELETE THE REFRESH TOKEN
    @POST("logout")
    fun deleteRefreshToken(
        @Body newDeleteRefreshTokenRequest: DeleteRefreshTokenRequest
    ): Call<DeleteRefreshTokenResponse>

    //Get Info from classification result
    @GET("info_ML")
    fun getHowToServeVeg(
        @Header("Authorization") token: String,
        @Query("result") query: String
    ): Call<infoMLResponse>

}