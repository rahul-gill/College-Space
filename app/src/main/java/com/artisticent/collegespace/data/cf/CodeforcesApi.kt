package com.artisticent.collegespace.data.cf

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET


interface CodeforcesApi {
    @GET("contest.list")
    fun getContestListAsync(): Deferred<Response<ContestList>>
}