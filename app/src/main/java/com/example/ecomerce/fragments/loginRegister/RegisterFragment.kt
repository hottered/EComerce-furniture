package com.example.ecomerce.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomerce.R
import com.example.ecomerce.data.User
import com.example.ecomerce.databinding.FragmentRegisterBinding
import com.example.ecomerce.utill.RegisterValidation
import com.example.ecomerce.utill.Resource
import com.example.ecomerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    
    private val TAG = "RegisterFragment"
    private lateinit var binding:FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.tvDoYouHaveAnAcc.setOnClickListener { 
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.apply { 
            buttonRegisterRegister.setOnClickListener{
                val user = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test",it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                        
                    }
                    else -> Unit
                    
                }
            }
        }
        lifecycleScope.launchWhenStarted { 
            viewModel.validation.collect{validation ->
                if(validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edEmailRegister.apply { 
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPasswordRegister.apply { 
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}