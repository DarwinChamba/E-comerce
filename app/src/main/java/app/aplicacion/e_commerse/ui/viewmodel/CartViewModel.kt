package app.aplicacion.e_commerse.ui.viewmodel

import android.system.Os.remove
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.data.model.FirebaseCommon
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.util.StateFirebase
import app.aplicacion.e_commerse.util.getProductPrice
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private val data: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
    private val _cartProducts =
        MutableStateFlow<StateFirebase<List<CartProduct>>?>(null)
    val cartProducts = _cartProducts.asStateFlow()


    val productPrice = cartProducts.map {
        when (it) {
            is StateFirebase.Succes -> {
                calculatePrice(it.dataUser!!)
            }

            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()


    private fun calculatePrice(dataUser: List<CartProduct>): Float {
        return dataUser.sumByDouble { cartProduct ->
            (cartProduct.products.offerPercentage.getProductPrice(cartProduct.products.price!!)
                    * cartProduct.quantity).toDouble()

        }.toFloat()
    }


    private var cardProductsList: MutableList<CartProduct> = mutableListOf()

    init {
        getCartProdcts()
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value?.dataUser?.indexOf(cartProduct)
        println("que indice es $index")

        if (index != null && index != -1) {
            // Obtén el ID del producto desde el objeto cartProduct
            val productId = cartProduct.products.id

            // Encuentra la referencia al producto en Firebase
            val productRef = data.getReference("userEcomerce")
                .child(auth.uid!!)
                .child("cart")
                .orderByChild("products/id")
                .equalTo(productId)

            productRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Itera sobre los resultados y elimina el producto
                        for (productSnapshot in snapshot.children) {
                            productSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    println("Producto eliminado exitosamente")
                                    // Opcional: actualiza tu lista local
                                    cardProductsList.remove(cartProduct)
                                    viewModelScope.launch {
                                        _cartProducts.emit(StateFirebase.Succes(cardProductsList))
                                    }
                                }
                                .addOnFailureListener { e ->
                                    println("Error al eliminar el producto: ${e.message}")
                                }
                        }
                    } else {
                        println("la ruta no existe")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error al acceder a la base de datos: ${error.message}")
                }
            })
        } else {
            println("ingresa porque no coincide ")
        }
    }


    private fun getCartProdcts() {
        viewModelScope.launch {
            _cartProducts.emit(StateFirebase.Loading())
        }
        data.getReference("userEcomerce").child(auth.uid!!).child("cart")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cardProductsList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val productList = i.getValue(CartProduct::class.java)
                            productList?.let {
                                cardProductsList.add(it)
                            }
                        }
                        viewModelScope.launch {
                            _cartProducts.emit(StateFirebase.Succes(cardProductsList))
                        }
                    } else {
                        viewModelScope.launch {
                            _cartProducts.emit(StateFirebase.Succes(emptyList()))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch { _cartProducts.emit(StateFirebase.Error(error.message)) }
                }
            })
    }


    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ) {
        val index = cartProducts.value?.dataUser?.indexOf(cartProduct)

        if (index != null && index != -1) {
            val id = cardProductsList[index].products.id
            println("este es el id $id")
            when (quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {

                    viewModelScope.launch { _cartProducts.emit(StateFirebase.Loading()) }

                    increseQuantity(id)
                }

                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(StateFirebase.Loading()) }
                    decreaseQuantity(id)
                }
            }
        }
    }

    private fun decreaseQuantity(id: String) {
        firebaseCommon.decraceQuantity(id) { result, exception ->
            if (exception != null) {
                viewModelScope.launch { _cartProducts.emit(StateFirebase.Error(exception.message.toString())) }
            }
        }
    }


    private fun increseQuantity(id: String) {
        firebaseCommon.increaseQuantity(id) { result, exception ->
            if (exception != null) {
                viewModelScope.launch { _cartProducts.emit(StateFirebase.Error(exception.message.toString())) }
            } else {
                // Vuelve a cargar el carrito para reflejar los cambios
                getCartProdcts()
            }
        }
    }


}