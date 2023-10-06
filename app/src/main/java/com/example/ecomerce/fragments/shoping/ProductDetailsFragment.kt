package com.example.ecomerce.fragments.shoping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomerce.R
import com.example.ecomerce.activites.ShoppingActivity
import com.example.ecomerce.adapters.ColorAdapter
import com.example.ecomerce.adapters.SizesAdapter
import com.example.ecomerce.adapters.ViewPager2Images
import com.example.ecomerce.data.CartProduct
import com.example.ecomerce.databinding.FragmentProductDetailsBinding
import com.example.ecomerce.utill.Resource
import com.example.ecomerce.utill.hideBottomNavigationView
import com.example.ecomerce.viewmodel.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy {
        ViewPager2Images()
    }
    private val sizeAdapter by lazy {
        SizesAdapter()
    }
    private val colorAdapter by lazy {
        ColorAdapter()
    }
    private var selectedColor : Int?=null
    private var selectedSize :String?=null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupViewPager()
        setupColorsRv()
        
        sizeAdapter.onItemClick = {
            selectedSize = it
        }
        colorAdapter.onItemClick = {
            selectedColor = it
        }
        binding.buttonAddToCard.setOnClickListener { 
            viewModel.addUpdateProductInCard(CartProduct(product,1,selectedColor,selectedSize))
        }
        
        lifecycleScope.launchWhenStarted { 
            viewModel.addToCard.collectLatest { 
                when(it){
                    is Resource.Loading -> {
                        binding.buttonAddToCard.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonAddToCard.revertAnimation()
                        Toast.makeText(requireContext(),"Product was added to cart",Toast.LENGTH_SHORT).show()

                    }
                    is Resource.Error -> {
                        binding.buttonAddToCard.revertAnimation()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDesc.text = product.description
            if(product.colors.isNullOrEmpty()){
                productColors.visibility = View.INVISIBLE
            }
            if(product.sizes.isNullOrEmpty()){
                tvProductsSize.visibility = View.INVISIBLE
            }
        }
        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizeAdapter.differ.submitList(it)
        }
        binding.imgClose.setOnClickListener { 
            findNavController().navigateUp()
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductsImages.adapter = viewPagerAdapter

        }
    }


}