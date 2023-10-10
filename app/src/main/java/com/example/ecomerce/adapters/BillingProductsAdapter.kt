package com.example.ecomerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomerce.R
import com.example.ecomerce.data.Address
import com.example.ecomerce.data.CartProduct
import com.example.ecomerce.databinding.AddressRvItemBinding
import com.example.ecomerce.databinding.BillingProductsRvItemBinding
import com.example.ecomerce.helper.getProductPrice

class BillingProductsAdapter : RecyclerView.Adapter<BillingProductsAdapter.BillingViewHolder>() {
    
    inner class BillingViewHolder(val binding: BillingProductsRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(billingProduct: CartProduct) {
            
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()

                val priceAfterPercentage = billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "$ ${String.format("%.2f",priceAfterPercentage)}"

                imageCartProductColor.setImageDrawable(ColorDrawable(billingProduct.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = billingProduct.selectedSize?:"".also { imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))}
            }
        }

    }

    private val diffCallback = object: DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billing = differ.currentList[position]
        holder.bind(billing)
    }

    var onClick : ((CartProduct) -> Unit)?=null

}