package com.example.wallpaperappson.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.wallpaperapppexelapi.model.Photo
import com.example.wallpaperappson.R
import com.example.wallpaperappson.databinding.ItemWallpaperBinding
import com.example.wallpaperappson.fragment.HomeFragmentDirections

class HomeRecyclerAdapter : RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>() {

    var dataList= listOf<Photo>()


    @SuppressLint("NotifyDataSetChanged")
    fun setDataRecycler(data1:List<Photo>){
        this.dataList=data1
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding :ItemWallpaperBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photographer=dataList!!.get(position).photographer
        val portreit=dataList.get(position).src.portrait
        val view=holder.itemView
        val dataListe=dataList.get(position)
        holder.binding.apply {
            Glide.with(view)
                .load(portreit)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_search_24)
                .into(imageView)

            textViewUserName.text=photographer
        }

        view.setOnClickListener {
            val intent=HomeFragmentDirections.actionHomeFragmentToDetailFragment(dataListe)
            Navigation.findNavController(view).navigate(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}