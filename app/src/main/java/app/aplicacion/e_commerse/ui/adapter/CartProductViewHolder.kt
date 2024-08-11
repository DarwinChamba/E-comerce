package app.aplicacion.e_commerse.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.databinding.CartProductItemBinding
import app.aplicacion.e_commerse.util.getProductPrice
import com.bumptech.glide.Glide

class CartProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CartProductItemBinding.bind(view)


    fun renderCartProducts(
        cartProduct: CartProduct,
        productSelected: ((CartProduct) -> Unit)?,
        onPlusClick: ((CartProduct) -> Unit)?,
        onMinClick: ((CartProduct) -> Unit)?
    ) {


        Glide.with(binding.imageView).load(cartProduct.products.images[0]).into(binding.imageView)
        binding.tvProductCartName.text = cartProduct.products.name
        binding.tvProductCartQuantity.text = cartProduct.quantity.toString()
        val priceAffterPercentage =
            cartProduct.products.offerPercentage.getProductPrice(cartProduct.products.price!!)
        binding.tvProductCartPrice.text = "${String.format("%.2f", priceAffterPercentage)}"
        binding.imageCartProductColor.setImageDrawable(
            ColorDrawable(
                cartProduct.selectedColor ?: Color.TRANSPARENT
            )
        )
        binding.tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
            binding.imageCartProductZise.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        itemView.setOnClickListener {
            productSelected?.invoke(cartProduct)
        }
        binding.imageMinus.setOnClickListener {
            onMinClick?.invoke(cartProduct)
        }
        binding.imagePlus.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }


    }
}