package com.example.owentime.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.owentime.R
import com.example.owentime.base.BaseFragment
import com.example.owentime.bean.Register
import com.example.owentime.checkLogin
import com.example.owentime.databinding.MineFragmentBinding
import com.example.owentime.start
import com.example.owentime.vm.MineViewModel
import com.gyf.immersionbar.ktx.immersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV

class MineFragment : BaseFragment(R.layout.mine_fragment) {

    companion object {
        fun newInstance() = MineFragment()
    }

    private val mViewModel by viewModels<MineViewModel>()
    private val mBinding by viewBinding(MineFragmentBinding::bind)
    private val mmkv=MMKV.defaultMMKV()
    private var mUserInfo:Register?=null

    override fun initData() {
        immersionBar {
            statusBarColor(R.color.white)
//            navigationBarColor(R.color.colorPrimary)
        }
    }

//    private fun user(){
//        mViewModel.user().observe(requireActivity(), Observer {
//            it?.run {
//                mBinding.tvUser.text=userInfo.nickname
//                mUserInfo=userInfo
//            }
//        })
//    }
}