package com.example.ecomerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomerce.data.CartProduct
import com.example.ecomerce.firebase.FirebaseCommon
import com.example.ecomerce.helper.getProductPrice
import com.example.ecomerce.utill.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    val productsPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }

            else -> null
        }
    }

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    init {
        getCartProduct()
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()
    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)//return index inside the cartProducatsStateFLow
        if(index!=null && index!=-1){
            val documentId = cartProductDocuments[index].id
            firestore.collection("user").document(auth.uid!!).collection("cart").document(documentId).delete()
        }
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
        val index = cartProducts.value.data?.indexOf(cartProduct)//return index inside the cartProducatsStateFLow
        /**
         * ubdex cold be equal to -1 if the function getcartProduct delays which will also delay the result we expect to be inside the [..cartProductsState]
         * we prevent form chasing we make the check..
         * **/

        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            when (quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
                    increasingQuantity(documentId)
                }


                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }
                        return
                    }
                    viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }
            }

        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId) { result, exception ->

            if (exception != null) {
                viewModelScope.launch {
                    _cartProduct.emit(Resource.Error(exception.message.toString()))
                }
            }

        }
    }

    private fun increasingQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId) { result, exception ->

            if (exception != null) {
                viewModelScope.launch {
                    _cartProduct.emit(Resource.Error(exception.message.toString()))
                }
            }

        }
    }
}