package app.aplicacion.e_commerse.ui.adapter.adapterImageViewPager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.databinding.ViewpagerImageBinding
import com.bumptech.glide.Glide

class ViewPagerViewHolder(view:View) :RecyclerView.ViewHolder(view){

    private val bindingPager=ViewpagerImageBinding.bind(view)
     fun render(image:String){
Glide.with(bindingPager.imageProductsDetails).load(image).into(bindingPager.imageProductsDetails)
     }


}
