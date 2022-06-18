package com.example.owentime.bean

import androidx.core.content.ContextCompat.getColor
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.example.owentime.App
import com.example.owentime.R
import com.example.owentime.databinding.ItemProductBinding
import com.example.owentime.databinding.LayoutFlexTagBinding
import com.example.owentime.databinding.LayoutProjectBinding
import com.example.owentime.databinding.LayoutWorksBinding
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
    val sku: List<Sku>? = listOf()
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
    val stock: Int? = 0,

    var selected:Boolean?=false
):ItemBind{
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding= LayoutFlexTagBinding.bind(holder.itemView)
        binding.tvSpecification.text=name
        if (selected == true){
            binding.tvSpecification.shapeDrawableBuilder.setSolidColor(getColor(binding.root.context,R.color.FE9520)).intoBackground()
            binding.tvSpecification.setTextColor(R.color.FE9520)
        }else{
            binding.tvSpecification.shapeDrawableBuilder.setSolidColor(getColor(binding.root.context,R.color.F5F5F5)).intoBackground()
            binding.tvSpecification.setTextColor(R.color.F4D4D4D)
        }
    }
}