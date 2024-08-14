package app.aplicacion.e_commerse.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.data.model.orden.OrderStatus
import app.aplicacion.e_commerse.data.model.orden.getOrderStatus
import app.aplicacion.e_commerse.databinding.FragmentOrderDetailBinding
import app.aplicacion.e_commerse.ui.adapter.BilingProductAdapter
import app.aplicacion.e_commerse.util.VerticalItemDecoration
import com.shuhart.stepview.StepView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment : Fragment(){
    private lateinit var binding:FragmentOrderDetailBinding
    private val billingProductAdapter by lazy { BilingProductAdapter() }
    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderRv()
        val order=args.order
        binding.apply {
            tvOrderId.text="Order # ${order.orderId}"
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                )
            )

            val currentOrderState=when(getOrderStatus(order.orderStatus)){
                OrderStatus.Ordered -> 0
                OrderStatus.Confirmed -> 1
                OrderStatus.Shipped -> 2
                OrderStatus.Delivered -> 3
                else->0

            }
            stepView.go(currentOrderState,false)

            if(currentOrderState == 3){
                stepView.done(true)
            }
            tvFullName.text=order.address.fullName
            tvAddress.text="${order.address.street} ${order.address.city}"
            tvPhoneNumber.text=order.address.phone
            tvTotalPrice.text="$ ${order.totalPrice}"

        }
        billingProductAdapter.diffBilling.submitList(order.products)
    }

    private fun setupOrderRv() {
        binding.rvProducts.apply {
            adapter=billingProductAdapter
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}