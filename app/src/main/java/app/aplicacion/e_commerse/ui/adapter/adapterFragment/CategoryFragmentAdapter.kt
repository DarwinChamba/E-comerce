package app.aplicacion.e_commerse.ui.adapter.adapterFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoryFragmentAdapter(
    private val list:List<Fragment>,
    val fragmentManager: FragmentManager,
    val lifecycle: Lifecycle
) :FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

}