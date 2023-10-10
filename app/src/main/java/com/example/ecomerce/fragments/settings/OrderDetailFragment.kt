package com.example.ecomerce.fragments.settings

import android.media.tv.TvTrackInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomerce.adapters.BillingProductsAdapter
import com.example.ecomerce.data.order.Order
import com.example.ecomerce.data.order.OrderStatus
import com.example.ecomerce.data.order.getOrderStatus
import com.example.ecomerce.databinding.FragmentOrderDetailBinding
import com.example.ecomerce.utill.VerticalItemDecoration

class OrderDetailFragment : Fragment() {
    
    private lateinit var binding:FragmentOrderDetailBinding
    private val billingAdapter by lazy { 
        BillingProductsAdapter()
    }
    private val args by navArgs<OrderDetailFragmentArgs>()
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val order = args.order
        
        setupOrderRv()
        binding.apply { 
            tvOrderId.text  ="Order #${order.orderId}"
            
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status
                )
            )
            val currentOrderState  = when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }
            stepView.go(currentOrderState,false)
            if(currentOrderState == 3){
                stepView.done(true)
            }
            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phoneNumber
            tvTotalPrice.text = "$ ${order.totalPrice}"
            
        }
        billingAdapter.differ.submitList(order.products)
    }

    private fun setupOrderRv() {
        binding.rvProducts.apply { 
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = billingAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }


}