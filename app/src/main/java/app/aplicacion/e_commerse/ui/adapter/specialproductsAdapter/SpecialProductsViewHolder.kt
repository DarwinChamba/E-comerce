package app.aplicacion.e_commerse.ui.adapter.specialproductsAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.databinding.SpecialRvItemBinding
import com.bumptech.glide.Glide

class SpecialProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = SpecialRvItemBinding.bind(view)

    fun renderSpecilaProducts(products: Products,productSelected:((Products)->Unit)?) {


    Glide.with(binding.btnAddToCart).load(products.images[0]).into(binding.imgAd)
        binding.tvAdName.text = products.name
        binding.tvAdPrice.text = products.price.toString()
        itemView.setOnClickListener {
            productSelected?.invoke(products)
        }

    }

}