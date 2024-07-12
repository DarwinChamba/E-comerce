package app.aplicacion.e_commerse.ui.adapter.specialproductsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.Products

class SpecialProductsAdapter : RecyclerView.Adapter<SpecialProductsViewHolder>() {

    val classDiff = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }

    }
    val diff = AsyncListDiffer(this, classDiff)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.special_rv_item, parent, false)
        )
    }

    override fun getItemCount()=diff.currentList.size

    private var product:((Products)->Unit)?=null
    fun setOnClickSpecialProducts(productSelected:((Products)->Unit)?){
        product=productSelected
    }
    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        holder.renderSpecilaProducts(diff.currentList[position],product)
    }

}