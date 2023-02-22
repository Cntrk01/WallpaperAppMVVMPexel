package com.example.wallpaperappson.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapppexelapi.model.Photo
import com.example.wallpaperappson.adapter.HomeRecyclerAdapter
import com.example.wallpaperappson.databinding.FragmentHomeBinding
import com.example.wallpaperappson.utils.LoadingDialog
import com.example.wallpaperappson.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private var homeAdapter =HomeRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        natureData()
        observeAllData()
        setupRecyclerView()

    }
    private fun natureData(){
         viewModel.setData("nature")

        binding.imageSearchview.setOnClickListener {
            val inttent=HomeFragmentDirections.actionHomeFragmentToSearchFragment2()
            findNavController().navigate(inttent)
        }
    }

    private fun setupRecyclerView(){
        homeAdapter= HomeRecyclerAdapter()
         binding.apply {
             recyclerView.adapter=homeAdapter
             recyclerView.layoutManager=GridLayoutManager(requireContext(),2)
         }
    }

    private fun observeAllData(){
        with(viewModel){
            data.observe(requireActivity(), Observer {
                if(it.isSuccessful){
                        if(it.body()!!.total_results !=0){
                            binding.loadCardView.isVisible=false
                            //veriler varmış recyclerviewa bunu vermem gerekiyormuş yoksa herşey çalışıyor.
                            binding.recyclerView.isVisible=true
                            homeAdapter.setDataRecycler(it.body()?.photos as List<Photo>)

                        }else{
                            binding.loadCardView.isVisible=false
                            binding.noData.isVisible=true
                            binding.recyclerView.isVisible=false

                        }
                }

            })

            progresBar.observe(requireActivity(), Observer {
                if(it){
                    binding.loadCardView.isVisible=true
                    binding.recyclerView.isVisible=false
                    binding.noData.isVisible=false
                }else{
                    binding.recyclerView.isVisible=true
                    binding.loadCardView.isVisible=false
                }
            })

            noData.observe(requireActivity(), Observer {
                binding.noData.isVisible=it
            })


        }
    }

}