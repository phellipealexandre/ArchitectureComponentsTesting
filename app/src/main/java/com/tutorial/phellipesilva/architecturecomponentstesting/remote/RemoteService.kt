package com.tutorial.phellipesilva.architecturecomponentstesting.remote

import com.tutorial.phellipesilva.architecturecomponentstesting.database.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteService {

    @GET("users/{userId}")
    fun getUserById(@Path("userId") userId: Int): Call<User>

}