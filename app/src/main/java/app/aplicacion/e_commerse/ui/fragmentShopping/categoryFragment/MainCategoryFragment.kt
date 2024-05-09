package app.aplicacion.e_commerse.ui.fragmentShopping.categoryFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.databinding.FragmentMainCategoryBinding
import app.aplicacion.e_commerse.ui.adapter.specialproductsAdapter.SpecialProductsAdapter
import app.aplicacion.e_commerse.ui.viewmodel.SpecialProductsViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import kotlinx.coroutines.flow.collect


class MainCategoryFragment : Fragment() {
    private lateinit var binding:FragmentMainCategoryBinding
    private lateinit var cadapter:SpecialProductsAdapter
    private lateinit var model:SpecialProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMainCategoryBinding.inflate(inflater,container,false)
        initRecycler()
        model=ViewModelProvider(requireActivity()).get(SpecialProductsViewModel::class.java)
        model.getDataFromRealtime()
        //observeList()
        return binding.root
    }

    private fun observeList() {
        lifecycleScope.launchWhenStarted {
            model.dataList.collect{
                when(it){
                    is StateFirebase.Error -> {

                    }
                    is StateFirebase.Loading -> {


                    }
                    is StateFirebase.Succes -> {

                        val imageList=it.dataUser
                        imageList?.let {
                            it.forEach {
                                println(it.toString())
                            }
                        }
                    }
                    null -> {

                    }
                }
            }
        }
    }

    private fun initRecycler() {
        cadapter= SpecialProductsAdapter()
        binding.recyclerSpecialProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter=cadapter
        }
    }


}