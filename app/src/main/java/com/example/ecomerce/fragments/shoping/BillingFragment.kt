package com.example.ecomerce.fragments.shoping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomerce.R
import com.example.ecomerce.adapters.AddressAdapter
import com.example.ecomerce.adapters.BillingProductsAdapter
import com.example.ecomerce.databinding.FragmentBillingBinding
import com.example.ecomerce.utill.HorizontalItemDecoration
import com.example.ecomerce.utill.Resource
import com.example.ecomerce.viewmodel.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment(R.layout.fragment_billing) {
    
    private lateinit var binding:FragmentBillingBinding
    private val addressAdapter by lazy { 
        AddressAdapter()
    }
    private val billingProductsAdapter by lazy {
        BillingProductsAdapter()
    }
    private val viewModel by viewModels<BillingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupBillingProductsRv()
        setupAddressRv()
        
        lifecycleScope.launchWhenStarted { 
            viewModel.address.collectLatest { 
                when(it){
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply { 
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
            
        }
    }
}