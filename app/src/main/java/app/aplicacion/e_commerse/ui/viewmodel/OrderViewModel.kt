package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.orden.Order
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _order = MutableStateFlow<StateFirebase<Order>?>(null)
    val order = _order.asStateFlow()


    fun placeOrder(order: Order) {
        viewModelScope.launch { _order.emit(StateFirebase.Loading()) }
        viewModelScope.launch {
            try {
                // Generar un ID único para la orden
                val orderId = UUID.randomUUID().toString()

                // Crear un mapa de actualizaciones
                val updates = mutableMapOf<String, Any?>()

                // Agregar la orden a la lista de órdenes del usuario
                updates["/userEcomerce/${auth.uid!!}/orders/$orderId"] = order

                // Agregar la orden a la lista general de órdenes
                updates["/orders/$orderId"] = order

                // Obtener los items del carrito
                val cartSnapshot = database.getReference("userEcomerce")
                    .child(auth.uid!!)
                    .child("cart")
                    .get()
                    .await()


                // Eliminar cada item del carrito
                cartSnapshot.children.forEach { childSnapshot ->
                    updates["/userEcomerce/${auth.uid!!}/cart/${childSnapshot.key}"] = null
                }
                // Ejecutar todas las actualizaciones de una vez
                database.reference.updateChildren(updates).await()


                _order.emit(StateFirebase.Succes(order))

            } catch (e: Exception) {
                _order.emit(StateFirebase.Error(e.message))
            }
        }


    }
}