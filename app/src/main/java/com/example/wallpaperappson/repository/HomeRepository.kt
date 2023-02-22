package com.example.wallpaperappson.repository

import com.example.wallpaperappson.api.WallpaperApi
import javax.inject.Inject

class HomeRepository @Inject
 constructor(private val api: WallpaperApi){
     suspend fun getApi(text:String)= api.getWallPaper(text)
}