package app.aplicacion.e_commerse.ui.fragment.fragmentShopping.fragmentDeatails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.aplicacion.e_commerse.core.ToastM
import app.aplicacion.e_commerse.core.hide
import app.aplicacion.e_commerse.core.hideBottomNavigation
import app.aplicacion.e_commerse.core.show
import app.aplicacion.e_commerse.data.model.CartProduct
import app.aplicacion.e_commerse.databinding.FragmentDetailsBinding
import app.aplicacion.e_commerse.ui.adapter.adapterImageViewPager.ViewPager2ImageAdapter
import app.aplicacion.e_commerse.ui.adapter.coloradapter.ColorsAdapters
import app.aplicacion.e_commerse.ui.adapter.coloradapter.SizesAdapter
import app.aplicacion.e_commerse.ui.viewmodel.DetailsViewModel
import app.aplicacion.e_commerse.util.StateFirebase
import kotlinx.coroutines.flow.collect

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var sizesAdapter: SizesAdapter
    private lateinit var colorAdapter: ColorsAdapters
    private lateinit var viewPagerAdapter: ViewPager2ImageAdapter
    private val args by navArgs<DetailsFragmentArgs>()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private lateinit var viewmodel :DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        initRecyclerSizes()
        initRecyclerColors()
        viewmodel=ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java)
        initViewPager()
        initProducts()
        binding.closeImage.setOnClickListener {
            findNavController().navigateUp()
        }
        hideBottomNavigation()
        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorAdapter.onItemClick = {
            selectedColor = it
        }
        binding.btnAddToCart.setOnClickListener {
            addCart()
        }

        observeAddCart()
        return binding.root
    }

    private fun observeAddCart() {
        lifecycleScope.launchWhenStarted {
            viewmodel.addToCart.collect{
                when(it){
                    is StateFirebase.Error ->{
                        binding.progresBarAddCart.hide()
                        ToastM(it.message.toString())
                        println("error al insertar ${it.message}")
                    }

                    is StateFirebase.Loading -> binding.progresBarAddCart.show()
                    is StateFirebase.Succes -> {
                        binding.progresBarAddCart.hide()
                        ToastM("registro agregado con exito")
                        viewmodel._addToCart.value=null
                    }
                    null ->{

                    }
                }
            }
        }
    }

    private fun addCart() {
        viewmodel.addUpdateProductInCart(CartProduct(args.products, 1, selectedColor, selectedSize))
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPager2ImageAdapter()
        binding.viewPagerProductImage.adapter = viewPagerAdapter
    }

    private fun initProducts() {
        binding.tvName.text = args.products.name
        binding.tvPrice.text = args.products.price.toString()
        binding.tvDesciption.text = args.products.description


        viewPagerAdapter.diffViewPager2.submitList(args.products.images)
        args.products.sizes?.let { sizesAdapter.diffutilColor.submitList(it) }
        args.products.colors?.let { colorAdapter.diffutilColor.submitList(it) }


    }

    private fun initRecyclerColors() {
        colorAdapter = ColorsAdapters()
        binding.rvColors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorAdapter
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerSizes() {
        sizesAdapter = SizesAdapter()
        binding.rvSizes.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sizesAdapter
            setHasFixedSize(true)
        }

    }


}