package app.aplicacion.e_commerse.ui.adapter.productsAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.Products

class ProductAdapter:RecyclerView.Adapter<ProductViewHolder>() {

    private val item=object :DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return  oldItem==newItem
        }

    }
    val producfDiff=AsyncListDiffer(this,item)

    private var lista= listOf<Products>()
    @SuppressLint("NotifyDataSetChanged")
    fun setListBestProducts(list:List<Products>){
        lista=list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_rv_item,parent,false))
    }

    //override fun getItemCount()=producfDiff.currentList.size
    override fun getItemCount()=lista.size

    private var setBestProductSelected :((Products)->Unit)? = null

    fun setOnClickBestProducts(productSelected:((Products)->Unit)?){
        setBestProductSelected=productSelected
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.renderProduct(lista[position],setBestProductSelected)

    }



}