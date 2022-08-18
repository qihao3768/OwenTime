package com.example.time_project.ui

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.drake.brv.annotaion.AnimationType
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.example.time_project.*
import com.example.time_project.base.BaseActivity
import com.example.time_project.bean.order.Course
import com.example.time_project.bean.order.CourseX
import com.example.time_project.databinding.ActivityCourseDetailBinding
import com.example.time_project.util.IntentExtra.Companion.courseDub
import com.example.time_project.util.IntentExtra.Companion.courseId
import com.example.time_project.util.IntentExtra.Companion.courseTitle
import com.example.time_project.util.IntentExtra.Companion.courseUrl
import com.example.time_project.util.IntentExtra.Companion.iproductId
import com.example.time_project.util.IntentExtra.Companion.position
import com.example.time_project.util.IntentExtra.Companion.shareImage
import com.example.time_project.util.TextViewLinesUtil
import com.example.time_project.vm.OwenViewModel
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.launch


class CourseDetailActivity : BaseActivity(R.layout.activity_course_detail) {
    private val mBinding by viewBinding (ActivityCourseDetailBinding::bind)

    private val mViewModel by viewModels<OwenViewModel>()
    var lines=1//行数
    override fun initData() {
        immersionBar {
            statusBarColor(R.color.white)
            keyboardEnable(true)
            statusBarDarkFont(true)
            fitsSystemWindows(true)
        }


        mBinding.detailTitle.leftView.fastClick {
            finish()
        }

        var show:Boolean=false
        mBinding.layoutShow.fastClick {
            if (show){
                mBinding.courseJianjie.maxLines=1
                mBinding.tvShow.text="展开"
                mBinding.ivShow.load(R.drawable.video_down_icon)
            }else{
                mBinding.courseJianjie.maxLines = lines
                mBinding.tvShow.text="收起"
                mBinding.ivShow.load(R.drawable.video_up_icon)
            }
            show=!show
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    /***
     * 获取课程列表
     */
    private fun getData(){
        mViewModel.getCourse(intent.iproductId.toString()).observe(
            this, Observer {
                it?.run {
                    when(code){
                        1000->{
                            val body: Course?=data
                            if (body==null){
                                show(null)
                            }else{
                                mBinding.detailTitle.title=body.name?:""
                                mBinding.courseJianjie.text=body.introduction?:""
                                var labels = body.tags.toString().split("、")
                                mBinding.courseLabels.setLabels(labels)
                                mBinding.courseJianjie.let { tv->
                                    tv.text=body.introduction?:""
                                    lines=TextViewLinesUtil.getTextViewLines(tv,tv.width)
                                    tv.maxLines=1
                                    tv.visibility=View.VISIBLE
                                }
                                mBinding.ivShow.load(R.drawable.video_down_icon)
//                                mBinding.ivCoursePic.load(body.imgHead?:"")
                                 lifecycleScope.launch{
                                    val bitmap=getImageBitmapByUrl(body.imgHead?:"")
                                     val width=bitmap?.width
                                     val height=bitmap?.height
                                     Log.d("width",width.toString())
                                     Log.d("height",height.toString())
                                     val scll= width?.div(height?:width)
                                     Log.d("scll",scll.toString())

                                     val constraintSet = ConstraintSet()
                                     constraintSet.setDimensionRatio(mBinding.ivCoursePic.id, "h,$scll")

                                     mBinding.ivCoursePic.load(body.imgHead?:"")
                                }

                                show(body.course)
                            }

                        }
                        401->{
                            toast("登录状态失效，请重新登录")
                            show(null)
                        }else->{
                        toast(message.toString())
                        show(null)
                        }
                    }
                }
            }
        )
    }

    /***
     * 展示课程列表
     */
    private fun show(data:List<CourseX>?){
        if (data.isNullOrEmpty()){
            //显示空视图
            mBinding.stateCourse.apply {
                emptyLayout=R.layout.empty_course
            }.showEmpty()
        }else{
            mBinding.listCourse.linear().setup {
                addType<CourseX>(R.layout.item_course)
                models=data
                onClick(R.id.layout_playing){
                    val model=getModel<CourseX>(modelPosition)
                    if (model.isLocked==1){
                        toast("请先观看上一节视频，完成解锁")
                    }else{
                        intent.courseTitle=model.name?:""
                        intent.courseUrl=model.url?:""//课程链接
                        intent.courseId=(model.id?:0).toString()//课程id
                        intent.iproductId=model.productId?:0
                        intent.courseDub=model.dubCourse?:""//配音视频链接，有内容表示这是一个需要配音的视频
                        intent.position=modelPosition?:0
                        intent.shareImage=model.image?:""
                        start(this@CourseDetailActivity,ExoplayerActivity().javaClass,intent)

                    }

                }
                setAnimation(AnimationType.SLIDE_BOTTOM)
            }
        }

    }

}