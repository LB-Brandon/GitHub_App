package com.brandon.github_app.network


import com.brandon.github_app.model.Repo
import com.brandon.github_app.model.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @GET("users/{username}/repos")
    fun getReposByUserName(
        @Path("username") username: String,
        @Query("page") page: Int
    ): Call<List<Repo>>

    @GET("search/users")
    fun getSearchUsers(@Query("q") query: String): Call<UserDTO>

}