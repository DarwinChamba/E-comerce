package app.aplicacion.e_commerse.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.databinding.BillingProductsRvItemBinding
import app.aplicacion.e_commerse.util.getProductPrice
import com.bumptech.glide.Glide

class BilingProductAdapter : RecyclerView.Adapter<BilingProductAdapter.BillingViewHolder>() {

    inner class BillingViewHolder(val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun renderBilling(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.products.images[0]).into(imageCartProduct)
                tvProductCartName.text=cartProduct.products.name
                tvBillingProductQuantity.text=cartProduct.quantity.toString()
                val priceAffterPercentage =
                    cartProduct.products.offerPercentage.getProductPrice(cartProduct.products.price!!)
                binding.tvProductCartPrice.text = "${String.format("%.2f", priceAffterPercentage)}"
                binding.imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
            }//
        }


    }

    private val billing = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.products.id == newItem.products.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val diffBilling = AsyncListDiffer(this, billing)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder(BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = diffBilling.currentList.size

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billingProduct = diffBilling.currentList[position]
        holder.renderBilling(billingProduct)
    }

}