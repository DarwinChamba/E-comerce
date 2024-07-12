package app.aplicacion.e_commerse.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.util.Category
import app.aplicacion.e_commerse.util.StateFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CategorViewModel(
    private val data: FirebaseDatabase,
    private val category: Category
) : ViewModel() {
    private val _offerProducts =
        MutableStateFlow<StateFirebase<List<Products>>?>(StateFirebase.Loading())
    val offerProducts: StateFlow<StateFirebase<List<Products>>?> = _offerProducts


    private val _bestProducts =
        MutableStateFlow<StateFirebase<List<Products>>?>(StateFirebase.Loading())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

    fun fetchBestProducts() {
        viewModelScope.launch {
            val reference = data.getReference("Products")
            val query = reference.orderByChild("category").equalTo(category.cateory)
            val listBestProducts = mutableListOf<Products>()

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listBestProducts.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val offerPercentage = i.child("offerPercentage").getValue(Double::class.java)
                            if (offerPercentage == null) {
                                val newProduct = i.getValue(Products::class.java)
                                newProduct?.let {
                                    listBestProducts.add(it)
                                }
                            }
                        }

                    }
                    viewModelScope.launch {
                        if(listBestProducts.isNullOrEmpty()){
                            _bestProducts.emit(StateFirebase.Succes(emptyList()))

                        }else{
                            _bestProducts.emit(StateFirebase.Succes(listBestProducts))
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        _bestProducts.emit(StateFirebase.Error(error.message))
                    }
                }
            })
        }
    }


    fun fetchOfferProducts() {
       viewModelScope.launch {

           val reference = data.getReference("Products")
           val query = reference.orderByChild("category").equalTo(category.cateory)
           val listOfferProducts = mutableListOf<Products>()

           query.addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   listOfferProducts.clear()
                   if (snapshot.exists()) {
                       for (i in snapshot.children) {
                           val offerPercentage =
                               i.child("offerPercentage").getValue(Double::class.java)
                           if (offerPercentage != null) {
                               val newProduct = i.getValue(Products::class.java)
                               newProduct?.let {
                                   listOfferProducts.add(it)
                               }
                           }
                       }
                   }
                   viewModelScope.launch {

                       if(listOfferProducts.isNullOrEmpty()){
                           _offerProducts.emit(StateFirebase.Succes(emptyList()))

                       }else{
                           _offerProducts.emit(StateFirebase.Succes(listOfferProducts))
                       }

                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   viewModelScope.launch {
                       _offerProducts.emit(StateFirebase.Error(error.message))
                   }


               }
           })


       }

    }




}