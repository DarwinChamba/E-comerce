package app.aplicacion.e_commerse.ui.adapter.coloradapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.Products
import app.aplicacion.e_commerse.databinding.ColorRvItemBinding
import app.aplicacion.e_commerse.databinding.SizeRvItemBinding

class SizesAdapter:RecyclerView.Adapter<SizesAdapter.SizeViewHolder>() {
    private var selectedPosition = -1

    inner class SizeViewHolder(private val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun render(size:String, position: Int) {

            binding.tvSize.text=size
            if (position == selectedPosition) { //color is selected
                binding.apply {
                    imageShadown.visibility = View.VISIBLE


                }
            }else{//color is not selected
                binding.apply {
                    imageShadown.visibility = View.INVISIBLE


                }
            }

        }
    }

    private val item = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val diffutilColor = AsyncListDiffer(this, item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(SizeRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = diffutilColor.currentList.size


    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size=diffutilColor.currentList[position]
        holder.render(size, position)

        holder.itemView.setOnClickListener{
            if (selectedPosition>=0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition=holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }
    var onItemClick:((String)->Unit)?= null


}


