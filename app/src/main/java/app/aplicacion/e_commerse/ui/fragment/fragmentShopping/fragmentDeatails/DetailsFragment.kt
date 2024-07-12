package app.aplicacion.e_commerse.ui.fragment.fragmentShopping.fragmentDeatails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import app.aplicacion.e_commerse.core.hideBottomNavigation
import app.aplicacion.e_commerse.databinding.FragmentDetailsBinding
import app.aplicacion.e_commerse.ui.adapter.adapterImageViewPager.ViewPager2ImageAdapter
import app.aplicacion.e_commerse.ui.adapter.coloradapter.ColorsAdapters
import app.aplicacion.e_commerse.ui.adapter.coloradapter.SizesAdapter

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var sizesAdapter: SizesAdapter
    private lateinit var colorAdapter: ColorsAdapters
    private lateinit var viewPagerAdapter: ViewPager2ImageAdapter
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        initRecyclerSizes()
        initRecyclerColors()
        initViewPager()
        initProducts()
        binding.closeImage.setOnClickListener {
            findNavController().navigateUp()
        }
        hideBottomNavigation()
        return binding.root
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPager2ImageAdapter()
        binding.viewPagerProductImage.adapter = viewPagerAdapter
    }

    private fun initProducts() {
        binding.tvName.text=args.products.name
        binding.tvPrice.text=args.products.price.toString()
        binding.tvDesciption.text=args.products.description


        viewPagerAdapter.diffViewPager2.submitList(args.products.images)
        args.products.sizes?.let { sizesAdapter.diffutilColor.submitList(it) }
        args.products.colors?.let { colorAdapter.diffutilColor.submitList(it) }


    }

    private fun initRecyclerColors() {
        colorAdapter = ColorsAdapters()
        binding.rvColors.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = colorAdapter
            setHasFixedSize(true)
        }
    }

    private fun initRecyclerSizes() {
        sizesAdapter = SizesAdapter()
        binding.rvSizes.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = sizesAdapter
            setHasFixedSize(true)
        }

    }


}