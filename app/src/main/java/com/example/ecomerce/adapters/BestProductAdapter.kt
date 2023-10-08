package com.example.ecomerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomerce.data.Product
import com.example.ecomerce.databinding.BestDealsRvItemBinding
import com.example.ecomerce.databinding.ProductRvItemBinding
import com.example.ecomerce.helper.getProductPrice

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>(){

    inner class BestProductViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView)
                    .load(product.images[0])
                    .into(imgProduct)
                    val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                    tvNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if(product.offerPercentage == null){
                    tvNewPrice.visibility = View.INVISIBLE
                }
                tvPrice.text = "$ ${product.price}"
                tvName.text = product.name
            }
        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product  = differ.currentList[position]
        holder.bind(product)
        
        holder.itemView.setOnClickListener { 
            OnClick?.invoke(product)
        }
    }

    var OnClick: ((Product) -> Unit)? = null
}