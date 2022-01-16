package com.ravi.spynedemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.ravi.spynedemo.model.GIFData

class GifRvAdapter : RecyclerView.Adapter<GifRvAdapter.ViewHolder>() {

    private var gifList = mutableListOf<GIFData>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem: View = layoutInflater.inflate(R.layout.gif_row, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val data = gifList[position]
        holder.bind(data, context,position+1)
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    fun setData(gifAry: List<GIFData>, isInit:Boolean) {
        if(isInit) gifList = gifAry as MutableList<GIFData> else  gifList.addAll(gifAry)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.ivGif)
        var tv: TextView = itemView.findViewById(R.id.tv)
        fun bind(data: GIFData, context:Context,index:Int){
            Glide.with(context)
                .load(data.images.downsizedSmall.mp4)
                .apply(
                    centerCropTransform()
                        .placeholder(R.drawable.placeholder_img)
                        .error(R.drawable.placeholder_img)
                )
                .into(imageView)

           imageView.setOnClickListener{
               openGifDetailActivity(context,data)
            }
            tv.text = "#$index"
        }

        private fun openGifDetailActivity(context: Context, data :GIFData){
            val intent = Intent(context as MainActivity,GifDetailActivity::class.java)
            intent.apply {
                putExtra("title",data.title?:"")
                putExtra("url",data.images.original.url?:"")
                putExtra("type",data.type)
                putExtra("rating",data.rating)
            }
            context.startActivity(intent)
        }
    }
}