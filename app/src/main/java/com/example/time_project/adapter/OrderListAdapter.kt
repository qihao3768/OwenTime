package com.example.time_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.time_project.R
import com.example.time_project.bean.order.OrderListData
import com.example.time_project.bean.order.OrderListModel
import com.example.time_project.databinding.ItemOrderBinding

/***
 * 订单列表
 */
class OrderListAdapter: PagingDataAdapter<OrderListData, OrderListAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<OrderListData>() {
            override fun areItemsTheSame(oldItem: OrderListData, newItem: OrderListData): Boolean {
                return oldItem.orderSn == newItem.orderSn
            }

            override fun areContentsTheSame(oldItem: OrderListData, newItem: OrderListData): Boolean {
                return oldItem == newItem
            }
        }
    }
     class ViewHolder(private val binding:ItemOrderBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(item:OrderListData?){
            item?.run {
                detail?.run {
                    binding.orderTitle.text= this[0].name?:""
                }

            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order,parent,false)
        val binding= ItemOrderBinding.bind(view)
        return ViewHolder(binding)
    }
}