package com.example.time_project.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.time_project.R
import com.example.time_project.adapter.ProductBannerAdapter
import com.example.time_project.databinding.ActivityDetailBinding
import com.example.time_project.databinding.ActivityProductDetailBinding
import com.example.time_project.widget.NumIndicator
import com.youth.banner.listener.OnPageChangeListener

class DetailActivity : AppCompatActivity() {
    private lateinit var adapter: ProductBannerAdapter
    private val mBinding by viewBinding (ActivityDetailBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var stringArrayListExtra = intent.getStringArrayListExtra("777")
        Log.e("TAG", "onCreate: ${stringArrayListExtra}" )
        stringArrayListExtra?.let { initTopBanner(it) }
    }


    private fun initTopBanner(headImage: List<String>) {
        adapter= ProductBannerAdapter(this,headImage)
        mBinding.groupBanner.addBannerLifecycleObserver(this)
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position!=0){
                        adapter.stopVideo()
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })//添加生命周期观察者
            .setAdapter(adapter,false)
            .start()
            .isAutoLoop(false)
            .indicator= NumIndicator(this)
    }
}