package dev.mauri.vilabs.main

import dev.mauri.vilabs.network.ReqresApi
import dev.mauri.vilabs.network.UsersResponse
import javax.inject.Inject

class UsersDataSource @Inject constructor(
    private val reqresApi: ReqresApi
) {

    private var nextPage = FIRST_PAGE

    suspend fun getUsersRemoteInfo(): Result<UsersResponse?> {
        return try {
            val usersResponse = reqresApi.getUsersInfo(nextPage)
            if (usersResponse.isSuccessful) {
                val pageFromServer = usersResponse.body()?.page
                val totalPages = usersResponse.body()?.total_pages
                nextPage =
                    if ((pageFromServer?.compareTo(totalPages ?: FIRST_PAGE) ?: FIRST_PAGE) >=
                        0
                    ) {
                        FIRST_PAGE
                    } else {
                        pageFromServer?.plus(1) ?: FIRST_PAGE
                    }
                Result.success(usersResponse.body())
            } else Result.failure(Exception("Bad things, http code not OK"))
        } catch (e: Exception) {
            Result.failure(e.cause ?: Exception("Network Issue, timeout, that sort of things"))
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}
