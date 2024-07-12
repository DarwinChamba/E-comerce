package app.aplicacion.e_commerse.ui.adapter.herenciaAdapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.databinding.ProductRvItemBinding
import app.aplicacion.e_commerse.ui.adapter.productsAdapter.ProductViewHolder
import com.bumptech.glide.Glide


class HerenciaProductAdapter :
    RecyclerView.Adapter<HerenciaProductAdapter.HerenciaPrdocutViewHolder>() {

    private var setProductSelected :((Products)->Unit)? = null

    fun setOnClickHereniaProducts(productSelected:((Products)->Unit)?){
        setProductSelected=productSelected
    }
   private val item=object : DiffUtil.ItemCallback<Products>(){
       override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
           return oldItem.id == newItem.id  && oldItem.name == newItem.name &&
           oldItem.offerPercentage == newItem.offerPercentage  && oldItem.images == newItem.images

       }

       override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
           return  oldItem==newItem
       }

   }
   val productHerenciafDiff= AsyncListDiffer(this,item)


    inner class HerenciaPrdocutViewHolder(private val productBinding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(productBinding.root) {
        fun renderHerencia(products: Products) {
            Glide.with(productBinding.imgProduct).load(products.images[0])
                .into(productBinding.imgProduct)
            productBinding.tvName.text = products.name
            productBinding.tvPrice.text = products.price.toString()
            productBinding.tvNewPrice.text = products.offerPercentage.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerenciaPrdocutViewHolder {
        return HerenciaPrdocutViewHolder(ProductRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount()=productHerenciafDiff.currentList.size

    override fun onBindViewHolder(holder: HerenciaPrdocutViewHolder, position: Int) {
        val products=productHerenciafDiff.currentList[position]
        holder.renderHerencia(products)
        holder.itemView.setOnClickListener {
            setProductSelected?.invoke(products)
        }
    }



}
