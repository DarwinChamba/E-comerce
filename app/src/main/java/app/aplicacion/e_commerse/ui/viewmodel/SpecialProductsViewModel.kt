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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecialProductsViewModel @Inject constructor(
    private val data: FirebaseDatabase
) : ViewModel() {

    private val getSpecialProducts =
        MutableStateFlow<StateFirebase<List<Products>>?>(null)
    val especialProducts: StateFlow<StateFirebase<List<Products>>?> = getSpecialProducts

    //bestDeals
    private val getBestDeals =
        MutableStateFlow<StateFirebase<List<Products>>?>(null)
    val bestDealsList: StateFlow<StateFirebase<List<Products>>?> = getBestDeals

    //products
    private val getBestProducts =
        MutableStateFlow<StateFirebase<List<Products>>?>(null)
    val bestProducts: StateFlow<StateFirebase<List<Products>>?> = getBestProducts
//create objet for PagginfInfo
    private val paggingInfo=PaggingInfo()

    //init
    init {
        getSpecialProductsFromDatabase()
        getBestProductsFromDatbase()
        getBestDealsFromRealtime()
    }

    //get special products from realtime database
    fun getSpecialProductsFromDatabase() {
        viewModelScope.launch {
            getSpecialProducts.emit(StateFirebase.Loading())
        }
        viewModelScope.launch {
            val query =
                data.getReference("Products").orderByChild("category").equalTo("special products")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mutableList = mutableListOf<Products>()

                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val dataList = data.getValue(Products::class.java)
                            dataList?.let {
                                mutableList.add(it)
                            }
                        }


                    }
                    viewModelScope.launch {
                        getSpecialProducts.emit(StateFirebase.Succes(mutableList))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        getSpecialProducts.emit(StateFirebase.Error(error.message.toString()))
                    }
                }

            })
        }

    }

    //get best deals from realtime database
    fun getBestDealsFromRealtime() {
        viewModelScope.launch {
            getBestDeals.emit(StateFirebase.Loading())
        }
        viewModelScope.launch {
            val query =
                data.getReference("Products").orderByChild("category").equalTo("specialDeals")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val mutableList = mutableListOf<Products>()

                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val dataList = data.getValue(Products::class.java)
                            dataList?.let {
                                mutableList.add(it)
                            }
                        }


                    }
                    viewModelScope.launch {
                        getBestDeals.emit(StateFirebase.Succes(mutableList))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        getBestDeals.emit(StateFirebase.Error(error.message))
                    }
                }

            })
        }

    }

    //get best products from realtime database

    fun getBestProductsFromDatbase() {
        if(!paggingInfo.endPage){
            viewModelScope.launch {
                getBestProducts.emit(StateFirebase.Loading())
            }
            viewModelScope.launch {
                val query = data.getReference("Products").orderByChild("category")
                    .equalTo("bestProduct").limitToFirst(paggingInfo.page *10)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val mutableList = mutableListOf<Products>()

                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val dataList = data.getValue(Products::class.java)

                                dataList?.let {
                                    mutableList.add(it)
                                }
                            }
                            paggingInfo.endPage = mutableList == paggingInfo.oldBestProduct
                            paggingInfo.oldBestProduct=mutableList

                        }
                        viewModelScope.launch {
                            getBestProducts.emit(StateFirebase.Succes(mutableList))
                        }
                        paggingInfo.page ++

                    }

                    override fun onCancelled(error: DatabaseError) {
                        viewModelScope.launch {
                            getBestProducts.emit(StateFirebase.Error(error.message.toString()))
                        }
                    }

                })
            }
        }


    }

    internal class PaggingInfo {
        var page: Int = 1
        var oldBestProduct:List<Products> = emptyList()
        var endPage:Boolean=false
    }


}