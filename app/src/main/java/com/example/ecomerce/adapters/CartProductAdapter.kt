package com.example.ecomerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomerce.data.CartProduct
import com.example.ecomerce.data.Product
import com.example.ecomerce.databinding.CartProductItemBinding
import com.example.ecomerce.databinding.SpecialRvItemBinding
import com.example.ecomerce.helper.getProductPrice

class CartProductAdapter  : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>(){
    
    inner class CartProductViewHolder( val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView)
                    .load(cartProduct.product.images[0])
                    .into(imgCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()
                
                val priceAfterPercentage = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f",priceAfterPercentage)}"
                
                imgCartProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = cartProduct.selectedSize?:"".also { imgCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))}
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            OnProductClick?.invoke(cartProduct)
        }
        holder.binding.imgPlus.setOnClickListener { 
            OnPlusClick?.invoke(cartProduct)
        }
        holder.binding.imgMinus.setOnClickListener {
            OnMinusClick?.invoke(cartProduct)
        }
    }

    var OnProductClick: ((CartProduct) -> Unit)? = null
    var OnPlusClick: ((CartProduct) -> Unit)? = null
    var OnMinusClick: ((CartProduct) -> Unit)? = null
    
    
}