package com.example.time_project.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.time_project.*
import com.example.time_project.base.BaseFragment
import com.example.time_project.databinding.MineFragmentBinding
import com.example.time_project.vm.MineViewModel
import com.gyf.immersionbar.ktx.immersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV
import kotlin.random.Random

class MineFragment : BaseFragment(R.layout.mine_fragment) {

    companion object {
        fun newInstance() = MineFragment()
    }

    private val mViewModel by viewModels<MineViewModel>()
    private val mBinding by viewBinding(MineFragmentBinding::bind)
    private val mmkv=MMKV.defaultMMKV()
    private var mToken=""//token


    override fun initData() {
        immersionBar {
            statusBarColor(R.color.white)
        }
        mBinding.layoutMyorder.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {
                start(requireActivity(),OrderListActivity().javaClass,false)
            }
        })
        mBinding.layoutTuihuo.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {

            }
        })
        mBinding.layoutYouhui.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {

            }
        })
        mBinding.layoutKefu.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {

            }
        })
        mBinding.layoutZhujiao.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {

            }
        })
        mBinding.layoutSetup.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {
                start(requireActivity(),SettingActivity().javaClass,false)
            }
        })
        mBinding.layoutDub.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {
                start(requireActivity(),WorksActivity().javaClass,false)
            }
        })

        mBinding.personalPhoto.checkLogin(requireActivity(), object : TodoListener {
            override fun todo() {
                start(requireActivity(),PerfectActivity().javaClass,false)
            }
        })

        getUser()

        LiveEventBus.get<String>("logout").observe(this, Observer {
            it?.run {
                getUser()
            }
        })
    }

    /***
     * 获取用户信息
     */
    private fun getUser(){
        mToken=mmkv.decodeString("token")?:""
        if (mToken.isNotEmpty()){
            mViewModel.getUser(mToken).observe(requireActivity(), Observer {
                it?.run {
                    mBinding.personalPhoto.load(photo?:"")
                    mBinding.personalName.text= if (name.isNullOrBlank()){
                        "Owen".plus(Random.nextInt(1,5))
                    }else{
                        name
                    }
                }
            })
        }else{
            mBinding.personalPhoto.setImageResource(R.drawable.logo)
            mBinding.personalName.text="登录/注册"
        }

    }
}