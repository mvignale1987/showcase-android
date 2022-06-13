package dev.mauri.vilabs.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReqresApi {

    @GET("/api/users")
    suspend fun getUsersInfo(
        @Query("page") page: Int,
    ): Response<UsersResponse>
}
