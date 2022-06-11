package com.example.owentime.ui

import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.example.owentime.R
import com.example.owentime.base.BaseActivity
import com.example.owentime.bean.WorksBean
import com.example.owentime.databinding.ActivityWorksBinding
import com.example.owentime.start
import com.example.owentime.util.IntentExtraString
import com.gyf.immersionbar.ktx.immersionBar

/***
 * 我的作品
 */
class WorksActivity : BaseActivity(R.layout.activity_works) {
    private val mBinding by viewBinding(ActivityWorksBinding::bind)
    companion object IntentOptions{
        var Intent.url by IntentExtraString("url")
    }
    override fun initData() {
        immersionBar {
            statusBarColor(R.color.white)
            keyboardEnable(true)
            statusBarDarkFont(true)
            fitsSystemWindows(true)
        }
        val data=getData()
        if (data.isNullOrEmpty()){
            mBinding.stateWorks.apply {
                emptyLayout=R.layout.empty_works
            }.showEmpty()
        }else{
            mBinding.rvWorks.linear().setup {
                addType<WorksBean>(R.layout.layout_works)
                onFastClick(R.id.btn_play){
                    intent.url="https://owen-time-test.oss-cn-beijing.aliyuncs.com/courses/cou/1643348728_216a94a44ba39a71.mp4"
                    start(this@WorksActivity,ExoplayerActivity().javaClass,intent)
                }
            }.models=getData()
        }


        mBinding.titleWorks.leftView.setOnClickListener {
            finish()
        }
    }

    private fun getData():MutableList<Any>{

        return mutableListOf<Any>().apply {

            for (i in 0..2) add(WorksBean("","绘声绘色","2022-01-01 18:00"))
        }
    }
}