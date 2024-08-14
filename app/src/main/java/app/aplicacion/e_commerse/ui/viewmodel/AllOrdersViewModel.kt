package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.orden.Order
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val data: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _allOrders = MutableStateFlow<StateFirebase<List<Order>>?>(null)
    val allOrder = _allOrders.asStateFlow()
    init {
        getAllOrders()
    }

    fun getAllOrders() {
        viewModelScope.launch { _allOrders.emit(StateFirebase.Loading()) }
        data.getReference("userEcomerce").child(auth.uid!!).child("orders").
                addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val listaOrders= mutableListOf<Order>()
                        if (snapshot.exists()) {
                            for (i in snapshot.children){
                                val orderObject=i.getValue(Order::class.java)
                                orderObject?.let {
                                    listaOrders.add(it)
                                }
                            }

                            println("lista de ordenes"+listaOrders)
                            viewModelScope.launch { _allOrders.emit(StateFirebase.Succes(listaOrders)) }

                        } else {
                            viewModelScope.launch { _allOrders.emit(StateFirebase.Error("ruta no valida para recuperar las ordeners")) }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        viewModelScope.launch { _allOrders.emit(StateFirebase.Error(error.message)) }
                    }

                })

    }


}