package com.example.ecomerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomerce.R
import com.example.ecomerce.adapters.BestProductAdapter
import com.example.ecomerce.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    private lateinit var offerAdapter: BestProductAdapter
    private lateinit var bestProducts: BestProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupOfferRv()
        setupBestProductsRv()
    }

    private fun setupBestProductsRv() {
        bestProducts = BestProductAdapter()
        binding.rvBestProducts.apply { 
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProducts
        }
    }

    private fun setupOfferRv() {
        offerAdapter = BestProductAdapter()
        binding.rvOffer.apply { 
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = offerAdapter
        }
    }
}