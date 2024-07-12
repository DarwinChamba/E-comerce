package app.aplicacion.e_commerse.ui.adapter.adapterImageViewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R

class ViewPager2ImageAdapter :RecyclerView.Adapter<ViewPagerViewHolder>(){

    private val item=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem ==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem== newItem
        }

    }
    val diffViewPager2=AsyncListDiffer(this,item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.viewpager_image,parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.render(diffViewPager2.currentList[position])
    }

    override fun getItemCount()=diffViewPager2.currentList.size

}