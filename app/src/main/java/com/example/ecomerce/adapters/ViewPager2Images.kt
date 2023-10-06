package com.example.ecomerce.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomerce.databinding.ViewpagerImageItemBinding

class ViewPager2Images : RecyclerView.Adapter<ViewPager2Images.ViewPage2ImagesViewHolder>() {
    inner class ViewPage2ImagesViewHolder(private val binding: ViewpagerImageItemBinding) 
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(imagePath:String){
                Glide.with(itemView)
                    .load(imagePath)
                    .into(binding.imageProductionDetails)
            }
        }
    
    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem

        }

    }
    
    val differ = AsyncListDiffer(this,diffCallback)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPage2ImagesViewHolder {
        return ViewPage2ImagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPage2ImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }
}