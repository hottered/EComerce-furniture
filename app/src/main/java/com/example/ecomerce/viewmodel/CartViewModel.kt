package com.example.ecomerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomerce.data.CartProduct
import com.example.ecomerce.firebase.FirebaseCommon
import com.example.ecomerce.utill.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProduct.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    init {
        getCartProduct()
    }

    private fun getCartProduct() {
        viewModelScope.launch {
            _cartProduct.emit(Resource.Loading())
        }
        firestore.collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Error(error?.message.toString()))
                    }
                } else {
                    cartProductDocuments = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProduct.emit(Resource.Success(cartProducts))
                    }
                }
            }
    }

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ) {
        val index =
            cartProducts.value.data?.indexOf(cartProduct)//return index inside the cartProducatsStateFLow
        /** 
         * ubdex cold be equal to -1 if the function getcartProduct delays which will also delay the result we expect to be inside the [..cartProductsState]
         * we prevent form chasing we make the check..
         * **/
        
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            
            when(quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    increasingQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {
                    decreaseQuantity(documentId)
                }
            }
            
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId) { result ,exception ->
            
            if(exception != null) {
                viewModelScope.launch { 
                    _cartProduct.emit(Resource.Error(exception.message.toString()))
                }
            }
            
        }
    }

    private fun increasingQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId) { result ,exception ->

            if(exception != null) {
                viewModelScope.launch {
                    _cartProduct.emit(Resource.Error(exception.message.toString()))
                }
            }

        }
    }
}