package com.example.owentime.bean
import coil.load
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.example.owentime.databinding.ItemOrderBinding
import com.example.owentime.databinding.ItemProductBinding
import com.example.owentime.load
import com.example.owentime.serializer.UserListSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName



@Serializable
data class HomeModel(
    @SerialName("banner")
    val banner: List<Banner>? = listOf(),
    @SerialName("product")
    val product: List<Product>? = listOf(),
    @SerialName("studying")
    val studying: List<Studying>? = listOf(Studying()),//记得改回来，先前没有数据，所以用了个string
    @SerialName("user")
    @Serializable(with = UserListSerializer::class)
    val user: List<User>? = listOf(User())
)

@Serializable
data class Banner(
    @SerialName("activity_links")
    val activityLinks: String? = "",
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("jump_type")
    val jumpType: Int? = 0,
    @SerialName("title")
    val title: String? = "",
    @SerialName("url")
    val url: String? = ""
)

@Serializable
data class Product(
    @SerialName("code")
    val code: String? = "",
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("img_head")
    val imgHead: String? = "",
    @SerialName("introduction")
    val introduction: String? = "",
    @SerialName("name")
    val name: String? = "",
    @SerialName("price_actual")
    val priceActual: String? = "",
    @SerialName("price_show")
    val priceShow: String? = "",
    @SerialName("user_count")
    val userCount: Int? = 0
):ItemBind{
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding= ItemProductBinding.bind(holder.itemView)
        binding.ivProduct.load(imgHead)
        binding.tvProductTitle.text=name
        binding.tvProductDesc.text=introduction
        binding.tvProductPrice02.text=priceShow
        binding.tvProductPtnum.text=userCount.toString().plus("人购买")
    }
}

@Serializable
data class User(
    @SerialName("code")
    val code: String? = "",
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("phone")
    val phone: String? = "",
    @SerialName("photo")
    val photo: String? = "",
    @SerialName("sex")
    val sex: Int? = 0
)
@Serializable
class Studying{}