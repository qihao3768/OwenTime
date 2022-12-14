package com.example.time_project.ui

import android.Manifest
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.time_project.*
import com.example.time_project.base.BaseActivity

import com.example.time_project.databinding.ActivityPerfectBinding
import com.example.time_project.ui.LoginActivity.IntentOptions.token
import com.example.time_project.util.CoilEngine
import com.example.time_project.util.IntentExtra.Companion.iBirthday
import com.example.time_project.util.IntentExtra.Companion.iHead
import com.example.time_project.util.IntentExtra.Companion.iSex
import com.example.time_project.util.IntentExtra.Companion.iSkip
import com.example.time_project.util.IntentExtra.Companion.iUserName
import com.example.time_project.vm.UserViewModel
import com.gyf.immersionbar.ktx.immersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopupext.listener.TimePickerListener
import com.lxj.xpopupext.popup.CommonPickerPopup
import com.lxj.xpopupext.popup.TimePickerPopup
import com.permissionx.guolindev.PermissionX
import com.tencent.mmkv.MMKV
import razerdp.basepopup.QuickPopupBuilder
import razerdp.basepopup.QuickPopupConfig
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


class PerfectActivity : BaseActivity(R.layout.activity_perfect) {
    private val mBinding by viewBinding(ActivityPerfectBinding::bind)

    private val mViewModel by viewModels<UserViewModel>()
    private val permissions = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var mmkv: MMKV
    private var sex:Int=1  //1????????? 2 ?????????

    override fun initData() {
        mmkv = MMKV.defaultMMKV()
        mBinding.titlePerfect.leftView.visibility=View.GONE
        immersionBar {
            statusBarView(mBinding.titlePerfect)
            keyboardEnable(false)
            statusBarDarkFont(false)
            fitsSystemWindows(false)
        }
        //??????skip??????
        if (intent.iSkip) {
            mBinding.titlePerfect.rightView.visibility=View.GONE
            mBinding.titlePerfect.leftView.visibility=View.VISIBLE
        }

        mBinding.titlePerfect.leftView.setOnClickListener {
            finish()
        }
        mBinding.titlePerfect.rightView.fastClick {
            start(this@PerfectActivity,MainActivity().javaClass,true)
        }
        mBinding.ivHead.setOnClickListener {
            getPermission()
        }
        mBinding.tvHis.setOnClickListener {
            showTimeDialog()
        }

        mBinding.tvSex.setOnClickListener {
            selectSex()
        }
        mBinding.btnSave.setOnClickListener {
            // TODO:  ??????????????????????????????
//            LiveEventBus.get("login",String::class.java).post("login")
            uploadInfo()
        }

        mBinding.titlePerfect.leftView.fastClick {
            finish()
        }
        mBinding.edtName.setText(intent.iUserName)
        mBinding.tvSex.text=intent.iSex.intToSex()
        mBinding.tvHis.text=intent.iBirthday
//        mBinding.ivHead.load(intent.iHead)
        intent.iHead?.run {
            mBinding.ivHead.load(this){
                placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
            }
        }

    }

    private fun getPermission() {
        PermissionX.init(this).permissions(permissions)
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????", "??????", "??????")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    showDialog()
                } else {
                    toast("???????????????????????????????????????????????????????????????")
                }
            }
    }
    //????????????????????????
    private fun showDialog(){
        QuickPopupBuilder.with(this).contentView(R.layout.dialog_photo)
            .config(
                QuickPopupConfig().gravity(Gravity.BOTTOM)
                    .withClick(R.id.tv_album,{
                        selectPic()
                    },true)
                    .withClick(R.id.tv_cancel,{

                    },true)
                    .fadeInAndOut(true)
                    .outSideDismiss(true)
                    .backpressEnable(true)
                    .autoLocated(false)
                    .withShowAnimation(
                        AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_BOTTOM)
                            .toShow()
                    )
                    .withDismissAnimation(
                        AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_BOTTOM)
                            .toDismiss()
                    )
            ).show()

    }
    //????????????
    private fun selectPic(){
        PictureSelector.create(this).openGallery(SelectMimeType.ofImage()).setImageEngine(CoilEngine()).setSelectionMode(1).forResult(
            object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    result?.run {
                        val path=result[0].realPath
                        mViewModel.upload(intent.token?:"",path).observe(this@PerfectActivity
                        ) {
                            it?.run {
                                toast("????????????")
                                mBinding.ivHead.load(photo)
                            }
                        }

                    }

                }

                override fun onCancel() {

                }
            })
    }

    private fun showTimeDialog(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

        val date = Calendar.getInstance()
        date[2000, 1] = 1
        val date2 = Calendar.getInstance()
//        date2[date2.get(Calendar.YEAR), 1] = 1
        val popup =
            TimePickerPopup(this) //
                                    .setDefaultDate(date2)  //????????????????????????
                                        .setYearRange(2000,date2.get(Calendar.YEAR)) //??????????????????
                                        .setDateRange(date, date2) //??????????????????
                .setTimePickerListener(object : TimePickerListener {
                    override fun onTimeChanged(date: Date?) {
                        //????????????
                    }

                    override fun onTimeConfirm(date: Date, view: View?) {
                        //??????????????????
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val birthday = dateFormat.format(date)
                       mBinding.tvHis.text= birthday
                    }
                })

        XPopup.Builder(this)
            .asCustom(popup)
            .show()
    }

    //????????????
    private fun selectSex(){
        val popup = CommonPickerPopup(this)
        val list = ArrayList<String>()
        list.add("?????????")
        list.add("?????????")
        popup.setPickerData(list)
            .setCurrentItem(1)
        popup.setCommonPickerListener { index, data ->
            mBinding.tvSex.text=list[index]
            sex=data.sexToInt()
        }
        XPopup.Builder(this)
            .asCustom(popup)
            .show()
    }
    private fun uploadInfo(){
        val name=mBinding.edtName.text.toString()
        val bir=mBinding.tvHis.text.toString()
        mViewModel.uploadInfo(intent.token?:"",name,sex,bir).observe(this, androidx.lifecycle.Observer {
            it?.run {
                toast("????????????")
                LiveEventBus.get<String>("refresh").post("refresh")
                start(this@PerfectActivity,MainActivity().javaClass,true)
            }
        })
    }

    private fun String.sexToInt():Int{
        return when(this){
            "?????????"->{
                1
            }else->{
                2
            }
        }
    }

    private fun Int.intToSex():String{
        return when(this){
            -1->{
                "?????????"
            }
            1->{
                "?????????"
            }else->{
                "?????????"
            }
        }
    }


}