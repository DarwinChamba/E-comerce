package app.aplicacion.e_commerse.ui.adapter.coloradapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.ColorRvItemBinding

class ColorsAdapters : RecyclerView.Adapter<ColorsAdapters.ColorViewHolder>() {
    private var selectedPosition = -1

    inner class ColorViewHolder(private val binding: ColorRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun render(color: Int, position: Int) {
            val imageDrawable: ColorDrawable =ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)
            if (position == selectedPosition) { //color is selected
                binding.apply {
                    imageShadown.visibility = View.VISIBLE
                    imagePicked.visibility = View.VISIBLE

                }
            }else{//color is not selected
                binding.apply {
                    imageShadown.visibility = View.INVISIBLE
                    imagePicked.visibility = View.INVISIBLE

                }
            }
        }
    }

   private  val item = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
    val diffutilColor = AsyncListDiffer(this, item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(ColorRvItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = diffutilColor.currentList.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color=diffutilColor.currentList[position]
        holder.render(color, position)
        holder.itemView.setOnClickListener{
            if (selectedPosition>=0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition=holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }
    var onItemClick:((Int)->Unit)?= null
}