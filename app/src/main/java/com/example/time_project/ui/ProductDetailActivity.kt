package com.example.time_project.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.donkingliang.labels.LabelsView
import com.drake.brv.utils.setup
import com.example.time_project.*
import com.example.time_project.adapter.ImageTitleHolder
import com.example.time_project.adapter.ProductBannerAdapter
import com.example.time_project.base.BaseActivity
import com.example.time_project.base.BasePopWindow
import com.example.time_project.bean.FlexTagModel
import com.example.time_project.bean.order.Sku
import com.example.time_project.databinding.ActivityProductDetailBinding
import com.example.time_project.databinding.LayoutSpecificationsBinding
import com.example.time_project.load
import com.example.time_project.util.IntentExtra.Companion.code
import com.example.time_project.util.IntentExtra.Companion.icode
import com.example.time_project.util.IntentExtra.Companion.icoupon
import com.example.time_project.util.IntentExtra.Companion.inum
import com.example.time_project.util.IntentExtra.Companion.iproductId
import com.example.time_project.util.IntentExtra.Companion.isku
import com.example.time_project.util.IntentExtra.Companion.iskuName
import com.example.time_project.util.IntentExtraInt
import com.example.time_project.util.IntentExtraString
import com.example.time_project.vm.OwenViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.gyf.immersionbar.ktx.immersionBar
import com.umeng.socialize.ShareAction
import com.umeng.socialize.ShareContent
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig

class ProductDetailActivity : BaseActivity(R.layout.activity_product_detail) {

    private lateinit var adapter: ProductBannerAdapter
    private val mBinding by viewBinding (ActivityProductDetailBinding::bind)

    private var buyDialog = BasePopWindow(this)

    private lateinit var spbinding: LayoutSpecificationsBinding

    private val viewModel by viewModels<OwenViewModel>()

    private var mSku :MutableList<Sku>?= mutableListOf()

    private var selectSku:Sku?=null

    private var mStock:Int=0//库存

    private lateinit var mShareAction:ShareAction//分享



    override fun initData() {
        immersionBar {
            statusBarView(R.id.product_title)
            keyboardEnable(false)
            statusBarDarkFont(false)
            fitsSystemWindows(false)
        }
        val view=layoutInflater.inflate(R.layout.layout_specifications,null)
        spbinding= LayoutSpecificationsBinding.bind(view)
        // TODO: 请求详情页数据,这里是模拟

        mBinding.layoutKtBuy.fastClick {
            showBuy()
        }

        mBinding.productTitle.rightView.fastClick {
            mShareAction.open()
        }

    mBinding.productTitle.leftView.fastClick {
        finish()
    }
        //详情
        getDetail(intent.code.toString())
        //分享
        share()
    }

    //初始化顶部banner

    private fun initTopBanner(headImage: List<String>, title: String) {
         adapter=ProductBannerAdapter(this,headImage)
        mBinding.groupBanner.addBannerLifecycleObserver(this)
            .addOnPageChangeListener(object :OnPageChangeListener{
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
            .indicator=CircleIndicator(this)
    }

    override fun onPause() {
        super.onPause()
        adapter.stopVideo()
    }
    /***
     * 显示商品规格
     */
    private fun showBuy(){

        mSku?.run {
            if (this.isEmpty()){
                //规格列表是空的
                spbinding.labelSpecification.visibility=View.GONE
                spbinding.listSpecification.visibility=View.GONE

            }else{
                //规格列表不是空的
                spbinding.tvSkuPrice.text= this[0].priceActual ?:""
                spbinding.ivSpecification.load(this[0].imgShow)
                when(size){
                    1->{
                        spbinding.labelSpecification.visibility=View.GONE
                        spbinding.listSpecification.visibility=View.GONE
                        selectSku=this[0]
                    }else->{
                    spbinding.listSpecification.setLabels(mSku
                    ) { _, _, data -> data.name }
                    spbinding.listSpecification.setOnLabelSelectChangeListener { _, data, _, _ ->
                        val sku=data as Sku
                        selectSku = sku
                        spbinding.tvSkuPrice.text=sku.priceActual?:""
                        spbinding.ivSpecification.load(sku.imgShow)
                    }
//
                    }
                }
            }
        }


        spbinding.tvPlus.setOnClickListener {
            val count=spbinding.tvCount.text.toString()
            spbinding.tvCount.text=count.toInt().plus(1).toString()
            spbinding.tvSub.setTextColor(R.color.F1A1A1A)
        }

        spbinding.tvSub.setOnClickListener {
            val count=spbinding.tvCount.text.toString()
            if (count!="1"){
                spbinding.tvCount.text=count.toInt().minus(1).toString()
            }else{
                spbinding.tvSub.setTextColor(R.color.CCCCCC)
            }

        }

        spbinding.ivClose.setOnClickListener {
            buyDialog.dismiss()
        }
        spbinding.btnBuy.setOnClickListener {
            val count=spbinding.tvCount.text.toString().toInt()
            val mStock:Int=selectSku?.stock?:0
            if (selectSku==null){
                toast("请选择规格")
                return@setOnClickListener
            }
            when(count){
                0->{
                    toast("没有库存")
                }
                in 1..mStock ->{
                    buyDialog.dismiss()
                    intent.icode=intent.code
                    intent.isku=selectSku?.id.toString()
                    intent.iskuName=selectSku?.name.toString()
                    intent.inum=count
                    intent.icoupon=""//优惠券暂时没有
                    intent.iproductId=selectSku?.productId?:0
                    start(this@ProductDetailActivity,UpOrderActivity().javaClass,intent)
                }
                else->{
                    toast("请选择购买数量")
                }

            }

        }

        buyDialog.contentView=spbinding.root
        buyDialog.setOutSideDismiss(true).setOutSideTouchable(true)
            .setPopupGravity(Gravity.BOTTOM)
            .setShowAnimation(
                AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_BOTTOM)
                    .toShow()
            )
            .setDismissAnimation(
                AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_BOTTOM)
                    .toDismiss()
            )
            .showPopupWindow()
    }



    /***
     * 获取详情数据
     */
    private fun getDetail(code:String){
        val settings: WebSettings = mBinding.groupDetailPic.settings
        settings.javaScriptEnabled = true //是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.useWideViewPort = true //设置此属性，可任意比例缩放。大视图模式
        settings.loadWithOverviewMode = true //和setUseWideViewPort(true)一起解决网页自适应问题
        mBinding.groupDetailPic.webViewClient= object : WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(p0: WebView, p1: String) {
            super.onPageFinished(p0, p1)
                val javascript = "javascript:function ResizeImages() {" +
                        "var myimg,oldwidth;" +
                        "var maxwidth = document.body.clientWidth;" +
                        "for(i=0;i <document.images.length;i++){" +
                        "myimg = document.images[i];" +
                        "if(myimg.width > maxwidth){" +
                        "oldwidth = myimg.width;" +
                        "myimg.width = maxwidth;" +
                        "}" +
                        "}" +
                        "}"
//                String width = String.valueOf(AppUtils.getPhoneWidthPixels(BuyTryStudyActivity.this));
                //                String width = String.valueOf(AppUtils.getPhoneWidthPixels(BuyTryStudyActivity.this));
                p0.loadUrl(javascript)
                p0.loadUrl("javascript:ResizeImages();")

            }
        }
        viewModel.getDetail(code).observe(this, Observer {
            it?.run {
                //val images:MutableList<String> = mutableListOf(imgHead?:"")
                initTopBanner(img_heads,name?:"")
                mBinding.groupDetailPic.loadUrl(detail?:"")
                //价格默认取规格列表的第一条
                mBinding.tvGoodsPrice.text="￥".plus(sku?.get(0)?.priceActual ?: "")
                mBinding.tvGoodsTitle.text=name?:""
                mBinding.tvGoodsDesc.text=introduction?:""
                mSku= sku?.toMutableList()
            }
        })
    }

    /***
     * 分享
     */
    private fun share() {
        mShareAction = ShareAction(this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).addButton(
            "复制链接","复制链接","umeng_socialize_copyurl","umeng_socialize_copyurl"
        ).setShareboardclickCallback { p0, p1 ->
            when (p0.mShowWord) {
                "复制文本" -> {
                    toast("复制文本")
                }
                "复制链接" -> {
//                    copyLink("https://www.owentime.cn/api/exp/expHtml")
                }
                else -> {
                    val umImage = UMImage(this, R.drawable.share_tiyan)
                    val shareContent = ShareContent()
                    shareContent.mText = "我正在给宝宝报名在家早教训练营，快来帮我点一下！"
                    ShareAction(this).withMedia(umImage)
                        .setPlatform(p1)
                        .setCallback(object : UMShareListener {
                            override fun onStart(p0: SHARE_MEDIA) {

                            }

                            override fun onResult(p0: SHARE_MEDIA) {
                                if (p0.name == "WEIXIN_FAVORITE") {
                                    toast("收藏成功")

                                } else {
                                    if (p0 != SHARE_MEDIA.MORE && p0 != SHARE_MEDIA.SMS
                                        && p0 != SHARE_MEDIA.EMAIL
                                        && p0 != SHARE_MEDIA.FLICKR
                                        && p0 != SHARE_MEDIA.FOURSQUARE
                                        && p0 != SHARE_MEDIA.TUMBLR
                                        && p0 != SHARE_MEDIA.POCKET
                                        && p0 != SHARE_MEDIA.PINTEREST
                                        && p0 != SHARE_MEDIA.INSTAGRAM
                                        && p0 != SHARE_MEDIA.GOOGLEPLUS
                                        && p0 != SHARE_MEDIA.YNOTE &&
                                        p0 != SHARE_MEDIA.EVERNOTE
                                    ) {
                                        toast("分享成功")
                                    }
                                }
                            }

                            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                                if (p0 != SHARE_MEDIA.MORE && p0 != SHARE_MEDIA.SMS && p0 != SHARE_MEDIA.EMAIL && p0 != SHARE_MEDIA.FLICKR && p0 != SHARE_MEDIA.FOURSQUARE && p0 != SHARE_MEDIA.TUMBLR && p0 != SHARE_MEDIA.POCKET && p0 != SHARE_MEDIA.PINTEREST && p0 != SHARE_MEDIA.INSTAGRAM && p0 != SHARE_MEDIA.GOOGLEPLUS && p0 != SHARE_MEDIA.YNOTE && p0 != SHARE_MEDIA.EVERNOTE) {
                                    toast("分享失败")
                                }

                            }

                            override fun onCancel(p0: SHARE_MEDIA?) {
                                toast("取消分享")
                            }
                        }).share()
                }
            }
        }
    }


}