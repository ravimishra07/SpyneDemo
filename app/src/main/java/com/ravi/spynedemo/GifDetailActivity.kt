package com.ravi.spynedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ravi.spynedemo.databinding.ActivityGifDetailBinding
import com.ravi.spynedemo.databinding.ActivityMainBinding

class GifDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGifDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGifDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
       supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.title = "GIF Details"

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        getDataFromUrl()
    }
   private fun getDataFromUrl(){
        val gifTitle = intent.getStringExtra("title") ?:"No title"
        val gifType= intent.getStringExtra("type")?:"No Source"
        val gifUrl = intent.getStringExtra("url")?:""
        val rating = intent.getStringExtra("rating")?:"No rating"
       setData(gifTitle,gifType,gifUrl,rating)
    }
   private fun setData(gifTitle: String,gifType:String,gifUrl:String, rating:String){
        binding.tvGifType.text = "Type: $gifType"
        binding.tvGifRating.text = "Rating: $rating"
        binding.tvGifTitle.text = "Title: $gifTitle"
       Glide.with(this)
           .load(gifUrl)
           .apply(
               RequestOptions.centerCropTransform()
               .placeholder(R.drawable.placeholder_img)
               .error(R.drawable.placeholder_img))
           .into(binding.ivGif)
    }
}