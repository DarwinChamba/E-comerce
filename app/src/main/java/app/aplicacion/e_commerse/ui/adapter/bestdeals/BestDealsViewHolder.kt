package app.aplicacion.e_commerse.ui.adapter.bestdeals

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.databinding.BestDealsRvItemBinding
import com.bumptech.glide.Glide

class BestDealsViewHolder(view: View):RecyclerView.ViewHolder(view) {
     val bestDeals=BestDealsRvItemBinding.bind(view)

    fun renderBestDeals(product: Products,productSelected:((Products)->Unit)?){
        Glide.with(bestDeals.imgBestDeal).load(product.images[0]).into(bestDeals.imgBestDeal)
        bestDeals.tvDealProductName.text=product.name
        bestDeals.tvNewPrice.text=product.offerPercentage.toString()
        bestDeals.tvOldPrice.text=product.price.toString()
        itemView.setOnClickListener {
            productSelected?.invoke(product)
        }
    }
}