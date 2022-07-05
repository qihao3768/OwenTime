package com.example.time_project.bean.home
import android.view.View
import coil.load
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.example.time_project.databinding.ItemProductBinding

import com.example.time_project.load

import com.google.gson.annotations.SerializedName
//import com.squareup.moshi.SerializedName
//import com.squareup.moshi.SerializedNameClass
import com.tencent.mmkv.MMKV


//@NameClass(generateAdapter = true)
data class HomeModel(
    @SerializedName("banner")
    val banner: List<Banner>? = listOf(),
    @SerializedName("product")
    val product: List<Product>? = listOf(),
    @SerializedName("studying")
    val studying: Studying? = Studying(),//记得改回来，先前没有数据，所以用了个string
    @SerializedName("user")
    val user: User? = User()
)

data class Banner(
    @SerializedName("activity_links")
    val activityLinks: String? = "",
    @SerializedName( "id")
    val id: Int? = 0,
    @SerializedName("jump_type")
    val jumpType: Int? = 0,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("product_code")
    val productCode:String?=""
)

data class Product(
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("img_head")
    val imgHead: String? = "",
    @SerializedName("introduction")
    val introduction: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("price_actual")
    val priceActual: String? = "",
    @SerializedName("price_show")
    val priceShow: String? = "",
    @SerializedName("user_count")
    val userCount: Int? = 0
):ItemBind{
    val token= MMKV.defaultMMKV().decodeString("token")
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding= ItemProductBinding.bind(holder.itemView)
        binding.ivProduct.load(imgHead)
        binding.tvProductTitle.text=name
        binding.tvProductDesc.text=introduction
        binding.tvProductPrice02.text=priceShow
        binding.tvProductPtnum.text=userCount.toString().plus("人购买")
        binding.btnGoto.visibility=View.GONE
    }
}

data class User(
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("phone")
    val phone: String? = "",
    @SerializedName("photo")
    val photo: String? = "",
    @SerializedName("sex")
    val sex: Int? = 0,
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("birthday")
    val birthday:String?=""
)
data class Studying(
    @SerializedName("courses_id")
    val coursesId: Int? = 0,
    @SerializedName("dub_course")
    val dubCourse: Any? = Any(),
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("product_id")
    val productId: Int? = 0,
    @SerializedName("time")
    val time: Int? = 0,
    @SerializedName("url")
    val url: String? = ""
)