package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.Address
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
class BillingViewModel @Inject constructor(
    private val  data:FirebaseDatabase,
    private val auth: FirebaseAuth
) :ViewModel(){

    private val _address= MutableStateFlow<StateFirebase<List<Address>>? >(null)
    val adress=_address.asStateFlow()

    init {
        getUserAddress()
    }

    fun getUserAddress(){
        viewModelScope.launch { _address.emit(StateFirebase.Loading()) }
        data.getReference("userEcomerce").child(auth.uid!!).child("address")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listAddress= mutableListOf<Address>()
                    if(snapshot.exists()){
                        for (i in snapshot.children){
                            val address=i.getValue(Address::class.java)
                            address?.let {
                                listAddress.add(it)
                            }
                        }
                        viewModelScope.launch { _address.emit(StateFirebase.Succes(listAddress)) }

                    }else{
                        viewModelScope.launch { _address.emit(StateFirebase.Error("Error la ruta es incorecta")) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   viewModelScope.launch { _address.emit(StateFirebase.Error(error.message)) }
                }

            }
        )
    }




}