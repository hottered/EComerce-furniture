package com.example.ecomerce.firebase

import com.example.ecomerce.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    
    private val cartCollection = firestore.collection("user")
        .document(auth.uid!!)
        .collection("cart")
    
    fun addProductToCart(cartProduct: CartProduct,onResult:(CartProduct?,Exception?) -> Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)
            }.addOnFailureListener { 
                onResult(null,it)
            }
    }
    
    fun increaseQuantity(documentId:String,onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction{transaction->
            
            val documentReference = cartCollection.document(documentId)
            val document = transaction.get(documentReference)
            val productObject = document.toObject(CartProduct::class.java)
            
            productObject?.let { 
                cartProduct->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentReference,newProductObject)
            }
            
        }.addOnSuccessListener {
            onResult(documentId,null)
            
        }.addOnFailureListener {
            onResult(null,it)
            
        }
    }
}