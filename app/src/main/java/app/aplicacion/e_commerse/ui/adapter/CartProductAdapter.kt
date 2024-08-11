package app.aplicacion.e_commerse.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.CartProduct

class CartProductAdapter:RecyclerView.Adapter<CartProductViewHolder>() {
    private val userDiff=object :DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.products.id == newItem.products.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return  oldItem== newItem
        }

    }
    val diffCart=AsyncListDiffer(this,userDiff)

    private var lista= mutableListOf<CartProduct>()
    fun setLista(list:MutableList<CartProduct>){
        lista=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
       return CartProductViewHolder(LayoutInflater.from(parent.context).inflate(
           R.layout.cart_product_item,parent,false
       ))
    }

    override fun getItemCount()=lista.size
    private var setCartProduct :((CartProduct)->Unit)? = null
    var onPlusClick :((CartProduct)->Unit)? = null
    var onMinClick :((CartProduct)->Unit)? = null



    fun setOnClickBestProducts(productSelected:((CartProduct)->Unit)?){
        setCartProduct=productSelected
    }
    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.renderCartProducts(lista[position],setCartProduct,onPlusClick,onMinClick)
    }
}