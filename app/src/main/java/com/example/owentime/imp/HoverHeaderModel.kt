package com.example.owentime.imp

import android.view.View
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.brv.item.ItemHover
import com.example.owentime.databinding.LayoutFlexTagBinding
import com.example.owentime.databinding.LayoutHoverHeaderBinding
import com.tencent.mmkv.MMKV

/**
 * @description 类描述
 * @author yangjie
 * @date 2022-06-03
 */
class HoverHeaderModel(val header:String):ItemHover,ItemBind {
    override var itemHover: Boolean=true
    val islogin= MMKV.defaultMMKV().decodeBool("islogin",false)
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding= LayoutHoverHeaderBinding.bind(holder.itemView)
        binding.tvHeader.text=header
        binding.tvSeemore.visibility=if (islogin){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

}