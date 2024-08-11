package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.data.model.FirebaseCommon

import app.aplicacion.e_commerse.util.StateFirebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val data: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val common: FirebaseCommon
) : ViewModel() {

   var _addToCart = MutableStateFlow<StateFirebase<CartProduct>?>(null)
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch { _addToCart.emit(StateFirebase.Loading()) }
        val userCartRef = data.getReference("userEcomerce").child(auth.uid!!).child("cart")


        userCartRef.orderByChild("products/id").equalTo(cartProduct.products.id).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // Si el producto ya existe, actualiza la cantidad
                    val productSnapshot = dataSnapshot.children.first()
                    val existingProduct = productSnapshot.getValue(CartProduct::class.java)

                    if (existingProduct?.products?.id == cartProduct.products.id) {
                        incressQuantity(productSnapshot.key!!, cartProduct)
                    }
                } else {
                    // Si el producto no existe, añádelo como un nuevo producto
                    addNewProductCart(cartProduct)
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(StateFirebase.Error(it.message.toString()))
                }
            }
    }//llave de cierre de la funcion addUpdateProduct




    private fun addNewProductCart(cartProduct: CartProduct) {
        common.addProductCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null) {
                    _addToCart.emit(StateFirebase.Succes(addedProduct))
                } else {
                    _addToCart.emit(StateFirebase.Error(e.toString()))
                }
            }
        }

    }

    private fun incressQuantity(id:String,cartProduct: CartProduct){
        common.increaseQuantity(id){_,exception->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(StateFirebase.Succes(cartProduct))
                } else {
                    _addToCart.emit(StateFirebase.Error(exception.toString()))
                }
            }

        }
    }


}