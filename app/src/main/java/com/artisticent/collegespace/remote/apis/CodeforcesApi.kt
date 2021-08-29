package com.artisticent.collegespace.remote.apis

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CodeforcesApi {
    @GET("/contest.list")
    suspend fun getContestList(): Deferred<Pair<String, List<ContestItem>>>
}