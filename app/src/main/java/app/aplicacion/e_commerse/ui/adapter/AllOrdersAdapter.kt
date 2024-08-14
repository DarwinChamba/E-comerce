package app.aplicacion.e_commerse.ui.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.aplicacion.e_commerse.R
import app.aplicacion.e_commerse.data.model.orden.Order
import app.aplicacion.e_commerse.data.model.orden.OrderStatus
import app.aplicacion.e_commerse.data.model.orden.getOrderStatus
import app.aplicacion.e_commerse.databinding.OrderItemBinding

class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text=order.orderId.toString()
                tvOrderDate.text=order.date
                val resources=itemView.resources
                val colorDrawable=when(getOrderStatus(order.orderStatus)){
                    OrderStatus.Canceled -> ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    OrderStatus.Confirmed -> ColorDrawable(resources.getColor(R.color.g_gray500))
                    OrderStatus.Delivered -> ColorDrawable(resources.getColor(R.color.g_gray500))
                    OrderStatus.Ordered -> ColorDrawable(resources.getColor(R.color.g_blue_gray200))
                    OrderStatus.Returned -> ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    OrderStatus.Shipped -> ColorDrawable(resources.getColor(R.color.g_blue))
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val orderDiffList = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
    val orderDiff = AsyncListDiffer(this, orderDiffList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = orderDiff.currentList.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orderDiff.currentList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    var onClick: ((Order) -> Unit)? = null

}