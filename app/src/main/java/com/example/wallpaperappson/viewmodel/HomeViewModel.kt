package com.example.wallpaperappson.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaperapppexelapi.model.WallPaperResponse
import com.example.wallpaperappson.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject
  constructor(private val repository: HomeRepository) :ViewModel(){

    private val _data=MutableLiveData<Response<WallPaperResponse>>()
    var data : LiveData<Response<WallPaperResponse>> = MutableLiveData()
    get() = _data

    private val _progresBar:MutableLiveData<Boolean> = MutableLiveData(true)
    val progresBar:LiveData<Boolean>
    get() = _progresBar

    private val _noData:MutableLiveData<Boolean> = MutableLiveData()
    val noData:LiveData<Boolean>
        get() = _noData


    init {
        setData("nature")
    }


    fun setData(s:String){
        _progresBar.value=true
        _noData.value=false
        viewModelScope.launch {
            repository.getApi(s).let {
                if(it.isSuccessful){
                    _data.postValue(it)
                    _progresBar.value=false
                    _noData.value=false
                }else{
                    _noData.value=true
                    _progresBar.value=false
                }
            }
        }
    }
}