package com.github.rahul_gill.collegespace.data.contestsApi

import retrofit2.Response
import retrofit2.http.GET

interface ContestApi {
    @GET("all")
    suspend fun getAllContestsList() : Response<List<ContestItem>>
}