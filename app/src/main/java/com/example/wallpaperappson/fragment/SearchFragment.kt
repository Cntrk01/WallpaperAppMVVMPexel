package com.example.wallpaperappson.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapppexelapi.model.Photo
import com.example.wallpaperappson.R
import com.example.wallpaperappson.adapter.HomeRecyclerAdapter
import com.example.wallpaperappson.adapter.SearchRecyclerAdapter
import com.example.wallpaperappson.databinding.FragmentHomeBinding
import com.example.wallpaperappson.databinding.FragmentSearchBinding
import com.example.wallpaperappson.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchAdapter = SearchRecyclerAdapter()
    private val viewModel: HomeViewModel by viewModels()

    var query=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageSearchview.setOnClickListener {
            binding.photoSearchRecyclerView.scrollToPosition(0)
            query=binding.inputSearch.text.toString()
            if(query ==""){
                Toast.makeText(requireContext(),"Query Is Not Empty",Toast.LENGTH_LONG).show()
            }else{
                viewModel.setData(query)
            }

        }

        binding.imageBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        observeAllData()
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        searchAdapter= SearchRecyclerAdapter()
        binding.apply {
            photoSearchRecyclerView.adapter=searchAdapter
            photoSearchRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        }
    }


    private fun observeAllData(){
        with(viewModel){
            data.observe(requireActivity(), Observer {
                if(it.isSuccessful){
                    if(it.body()!!.total_results !=0){
                        binding.loadCardView.isVisible=false
                        //veriler varmış recyclerviewa bunu vermem gerekiyormuş yoksa herşey çalışıyor.
                        binding.photoSearchRecyclerView.isVisible=true
                        searchAdapter.setDataRecycler(it.body()?.photos as List<Photo>)

                    }else{
                        binding.loadCardView.isVisible=false
                        binding.noData.isVisible=true
                        binding.photoSearchRecyclerView.isVisible=false

                    }
                }

            })

            progresBar.observe(requireActivity(), Observer {
                if(it){
                    binding.loadCardView.isVisible=true
                    binding.photoSearchRecyclerView.isVisible=false
                    binding.noData.isVisible=false
                }else{
                    binding.photoSearchRecyclerView.isVisible=true
                    binding.loadCardView.isVisible=false
                }
            })

            noData.observe(requireActivity(), Observer {
                binding.noData.isVisible=it
            })


        }
    }

    //onStart methodu ile resime tıklayıp geri basınca veri gelmeye devam edecek.
    override fun onStart() {
        super.onStart()
        if(query!=""){
            viewModel.setData(query)
        }else{
            viewModel.setData("dog")
        }

    }

    //Mesela searchdan home dönünce tekrar buraya gelince bize query boşsa dog resimleri getieriyor.
    //Bu olmazsa proje home ekranındaki nature fotoğraflarını getiriyor.
    override fun onResume() {
        super.onResume()
        if(query!=""){
            viewModel.setData(query)
        }else{
            viewModel.setData("dog")
        }
    }
}