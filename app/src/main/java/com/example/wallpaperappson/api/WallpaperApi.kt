package com.example.wallpaperappson.api

import com.example.wallpaperapppexelapi.model.WallPaperResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WallpaperApi {
    @Headers("Authorization: YOUR API KEY")
    @GET("search")
    suspend fun getWallPaper(
        @Query("query")query:String,
        @Query("per_page")perpage:Int=80
    ) : Response<WallPaperResponse>
}