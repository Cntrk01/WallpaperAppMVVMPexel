package com.example.wallpaperappson.fragment

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.wallpaperappson.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.wallappToolbar.title= args.srcPortreit.photographer
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Glide.with(this).load(args.srcPortreit.src.portrait).into(binding.photoView)
        binding.photografher.text=args.srcPortreit.photographer
        binding.photographerId.text=args.srcPortreit.photographer_id.toString()
        binding.textView4.text=args.srcPortreit.alt

        binding.setWallpaperButton.setOnClickListener {
           setWallpaperImage()
        }

        binding.downloadButton.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
            downloadImage()
            Toast.makeText(requireContext(),"Download Started",Toast.LENGTH_LONG).show()        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun setWallpaperImage(){

        GlobalScope.launch (Dispatchers.IO){
            val result : Bitmap = Picasso.get().load(args.srcPortreit.src.portrait).get()
            val wallpaperManager= WallpaperManager.getInstance(requireContext())

            try{
                wallpaperManager.setBitmap(result)
                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(requireContext(),"Wallpaper Change Succesfully",
                        Toast.LENGTH_LONG).show()
                })



            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun downloadImage(){
//        val url=args.srcPortreit.src.original
//        val imageInfo=args.srcPortreit
//        val dm=activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
//            val request=DownloadManager.Request(Uri.parse(url))
//                .setTitle(imageInfo.photographer)
//                .setDescription("")
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setAllowedOverMetered(true)
//            val saveGallery=ContentValues()
//            saveGallery.put("Info",imageInfo.alt)
//            saveGallery.put("Photographer",imageInfo.photographer)
//            saveGallery.put("PhotographerID",imageInfo.photographer_id)
//            saveGallery.put("Image",imageInfo.src.original)
//            dm.enqueue(request)

            GlobalScope.launch(Dispatchers.IO) {
               val result : Bitmap = Picasso.get().load(args.srcPortreit.src.portrait).get()
               saveToGallery(result)
           }
        }catch (e:Exception){
            Toast.makeText(requireContext(),"Image Download Failed",Toast.LENGTH_LONG).show()
        }
    }

    //galeriye eklemek için
    private fun saveToGallery(bitmap: Bitmap){

        val imageName="${args.srcPortreit.photographer}.jpg"
        var fos : OutputStream?=null
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            requireContext().contentResolver?.also{resolver->
                val contentValues=ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME,args.srcPortreit.photographer+args.srcPortreit.photographer_id)
                    put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
                }
                val imageUri : Uri?=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                fos=imageUri.let {
                    resolver.openOutputStream(it!!)
                }
            }


        }else{
            val imageDirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image=File(imageDirectory,imageName)
            fos=FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,it)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}