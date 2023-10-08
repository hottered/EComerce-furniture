package com.example.ecomerce.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecomerce.R
import com.example.ecomerce.databinding.ActivityShoppingBinding
import com.example.ecomerce.utill.Resource
import com.example.ecomerce.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.checkerframework.common.subtyping.qual.Bottom

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    
//    val binding by lazy { 
//        ActivityShoppingBinding.inflate(layoutInflater)
//    }
    private lateinit var binding : ActivityShoppingBinding
    
    val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_shopping)
        setContentView(binding.root)
        
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        
        lifecycleScope.launchWhenStarted { 
            viewModel.cartProducts.collectLatest { 
                when(it){
                    is Resource.Success -> {
                        val count = it.data?.size?:0
                        val bottomnavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomnavigation.getOrCreateBadge(R.id.cartFragment).apply { 
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit
                }
            }
        }
        
    }
}