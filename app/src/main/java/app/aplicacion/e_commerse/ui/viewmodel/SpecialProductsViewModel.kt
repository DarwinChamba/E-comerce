package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.data.model.User
import app.aplicacion.e_commerse.util.FirebaseState
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpecialProductsViewModel @Inject constructor(
    private val data: DatabaseReference
) : ViewModel() {

    private val getDataFromRealtimeDatabase =
        MutableStateFlow<StateFirebase<List<Products>>?>(null)
    val dataList: StateFlow<StateFirebase<List<Products>>?> = getDataFromRealtimeDatabase

    fun getDataFromRealtime() {
        viewModelScope.launch {
            val query = data.orderByChild("images")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mutableList = mutableListOf<Products>()
                    viewModelScope.launch {
                        getDataFromRealtimeDatabase.emit(StateFirebase.Loading())
                    }
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(Products::class.java)
                        data?.let {
                            mutableList.add(it)
                        }

                    }
                    viewModelScope.launch {
                        getDataFromRealtimeDatabase.emit(StateFirebase.Succes(mutableList))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}