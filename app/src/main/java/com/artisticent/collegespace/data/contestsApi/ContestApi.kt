package com.artisticent.collegespace.data.contestsApi

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ContestApi {
    @GET("all")
    fun getAllContestsListAsync() : Deferred<Response<List<ContestItem>>>
}