package app.aplicacion.e_commerse.ui.fragmentShopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.FragmentHomeBinding
import app.aplicacion.e_commerse.ui.adapter.adapterFragment.CategoryFragmentAdapter
import app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment.ChairFragment
import app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment.CuoboardFragment
import app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment.FurnitureFragement
import app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment.MainCategoryFragment
import app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment.TableFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        listCategory()
        return binding.root
    }

    private fun listCategory() {
        val list= arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CuoboardFragment(),
            TableFragment(),
            FurnitureFragement()
        )
        val adapter=CategoryFragmentAdapter(list,childFragmentManager,lifecycle)
        binding.viewPager2.adapter=adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){ tab: TabLayout.Tab, position: Int ->
            when(position){
                0-> tab.text="Main"
                1-> tab.text="Chair"
                2-> tab.text="CuoBoard"
                3-> tab.text="Table"
                4-> tab.text="Furniture"
            }

        }.attach()
    }


}