package app.aplicacion.e_commerse.ui.adapter.productsAdapter

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.databinding.ProductRvItemBinding
import app.aplicacion.e_commerse.util.getProductPrice
import com.bumptech.glide.Glide

class ProductViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val productBinding= ProductRvItemBinding.bind(view)

    fun renderProduct(products: Products,productSelected:((Products)->Unit)?){
        Glide.with(productBinding.imgProduct).load(products.images[0]).into(productBinding.imgProduct)
        productBinding.tvName.text=products.name
        val priceAfterOffer=products.offerPercentage.getProductPrice(products.price!!)
        productBinding.tvNewPrice.text="$ ${String.format("% .2f",priceAfterOffer)}"
        productBinding.tvPrice.text= Paint.STRIKE_THRU_TEXT_FLAG.toString()
        productBinding.tvPrice.text=products.price.toString()

        productBinding.tvNewPrice.text=products.offerPercentage.toString()
        itemView.setOnClickListener {
            productSelected?.invoke(products)
        }
    }

}