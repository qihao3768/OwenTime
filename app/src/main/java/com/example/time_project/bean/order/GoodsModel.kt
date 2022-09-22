package com.example.time_project.bean.order

import androidx.core.content.ContextCompat.getColor
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.example.time_project.R
import com.example.time_project.databinding.ItemProductBinding
import com.example.time_project.databinding.LayoutFlexTagBinding
import com.google.gson.annotations.SerializedName


/**
 * @description 类描述
 * @author yangjie
 * @date 2022-06-03
 */
data class GoodsModel(val title:String,val desc:String,val price:String,val count:String): ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding= ItemProductBinding.bind(holder.itemView)
        binding.tvProductTitle.text=title
        binding.tvProductPrice02.text=price
        binding.tvProductPtnum.text=count
        binding.tvProductDesc.text=desc
    }

}

//商品详情
data class GoodsDetail(
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("detail")
    val detail: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("img_head")
    val imgHead: String? = "",
    @SerializedName("introduction")
    val introduction: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("sku")
    val sku: List<Sku>? = listOf(),
    @SerializedName("img_heads")
    val img_heads: List<String> = listOf()
)

data class Sku(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("img_show")
    val imgShow: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("price_actual")
    val priceActual: String? = "",
    @SerializedName("product_id")
    val productId: Int? = 0,
    @SerializedName("stock")
    val stock: Int? = 0
)
