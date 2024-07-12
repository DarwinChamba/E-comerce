package app.aplicacion.e_commerse.ui.adapter.bestdeals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.Products

class BestDealtsAdapter:RecyclerView.Adapter<BestDealsViewHolder>() {
    private val itemSpecial=object :DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem ==newItem
        }

    }
    val diffBestDeals=AsyncListDiffer(this,itemSpecial)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.best_deals_rv_item,parent,false
        ))
    }

    override fun getItemCount()=diffBestDeals.currentList.size

    private var setBestDealsSelected :((Products)->Unit)? = null

    fun setOnClickDealsProducts(productSelected:((Products)->Unit)?){
        setBestDealsSelected=productSelected
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        holder.renderBestDeals(diffBestDeals.currentList[position],setBestDealsSelected)
    }
}