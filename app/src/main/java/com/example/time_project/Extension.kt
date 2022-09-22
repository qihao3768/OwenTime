package com.example.time_project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
//import com.example.owentime.bean.Register
import com.example.time_project.ui.LoginActivity

import com.tencent.mmkv.MMKV
import java.util.*
import kotlin.system.exitProcess

fun ImageView.load(url:String){
    val uri=Uri.parse(url)
    this.load(uri)
}

fun Activity.start(activity:Activity,clazz: Class<Activity>,finish: Boolean){
 start01(activity, clazz, finish)
}

fun Activity.start(activity: Activity, clazz: Class<Activity>, key: String, value: Any){
    start03(activity, clazz, key, value)
}

fun Activity.start(activity: Activity, clazz: Class<Activity>, params:HashMap<String,String>){
    start02(activity, clazz, params)
}

fun Activity.start(activity: Activity, clazz: Class<Activity>,intent: Intent){
    intent.setClass(activity,clazz)
    this.startActivity(intent)
}

fun Fragment.start(activity:Activity,clazz: Class<Activity>,finish: Boolean){
    start01(activity, clazz, finish)
}

fun Fragment.start(activity: Activity, clazz: Class<Activity>, key: String, value: Any){
   start03(activity, clazz, key, value)
}

fun Fragment.start(activity: Activity, clazz: Class<Activity>, params:HashMap<String,String>){
    start02(activity, clazz, params)
}

fun Fragment.start(activity: Activity, clazz: Class<Activity>,intent: Intent){
    intent.setClass(activity,clazz)
    this.startActivity(intent)
}

private fun start01(activity:Activity,clazz: Class<Activity>,finish:Boolean){
    val intent = Intent(activity,clazz)
    activity.startActivity(intent)
    if (finish){
        activity.finish()
    }

}

private fun start02(activity:Activity,clazz: Class<Activity>,params:HashMap<String,String>){
    val intent = Intent(activity,clazz)
    params.forEach {
        intent.putExtra(it.key,it.value.toString())
    }
    activity.startActivity(intent)
}
private fun start03(activity:Activity,clazz: Class<Activity>,key:String,value: Any){
    val intent = Intent(activity,clazz)

    when(value){
        is String->{
            intent.putExtra(key,value.toString())
        }
        is Int->{
            intent.putExtra(key,value.toInt())
        }
        is Double->{
            intent.putExtra(key,value.toDouble())
        }
    }

    activity.startActivity(intent)
}

/***
 * 防止快速点击
 */
fun View.fastClick(listener: (view: View) -> Unit) {
    val minTime = 1000L
    var lastTime = 0L
    this.setOnClickListener {
        val tmpTime = System.currentTimeMillis()
        if (tmpTime - lastTime > minTime) {
            lastTime = tmpTime
            listener.invoke(this)
        }
    }
}


//fun ViewGroup.checkLogin(msg: String):String{
//    val userInfo=MMKV.defaultMMKV().decodeParcelable("user",Register::class.java)
//    return if (userInfo==null){
//        toast(msg)
//        msg
//    }else{
//        ""
//    }
//}
//检查登录
fun ViewGroup.checkLogin(context:Context,todoListener: TodoListener){

    this.setOnClickListener {
        val token :String?=MMKV.defaultMMKV().decodeString("token","")
         if (token.isNullOrBlank()){
             start01(context as Activity,LoginActivity().javaClass,false)
        }else{
             todoListener.todo()
        }
    }

}

fun View.checkLogin(context:Context,todoListener: TodoListener){

    this.setOnClickListener {
        val token :String?=MMKV.defaultMMKV().decodeString("token","")
        if (token.isNullOrBlank()){
            start01(context as Activity,LoginActivity().javaClass,false)
        }else{
            todoListener.todo()
        }
    }
}
val toast = Toast(App.getContext())
fun toast(msg:String){

    val inflate =View.inflate(App.getContext(), R.layout.layout_toast,null )//自定义布局
    inflate.findViewById<TextView>(R.id.toast).text=msg
    toast.view = inflate //将自定义view设置给toast
    toast.setGravity(Gravity.BOTTOM, 0, 30) //自定义位置
    toast.show()

}

 fun EditText.checked(msg:String):String?{
    return if (this.text.isNullOrBlank()){
        toast(msg)
        null
    }else{
        this.text.toString()
    }
}
fun EditText.checkLength(length:Int,msg:String):String?{
    return if (this.text.isNullOrBlank() || this.text.length!=length){
        toast(msg)
        null
    }else{
        ""
    }
}

fun TextView.checked(msg:String):String?{
    return if (this.text.isNullOrBlank()){
        toast(msg)
        null
    }else{
        this.text.toString()
    }
}
interface TodoListener{
    fun todo()
}
private var exitTime=0L
fun Activity.exit(keyCode:Int,event:KeyEvent,msg: String,exit:Boolean):Boolean{
    if (keyCode == KeyEvent.KEYCODE_BACK && event.action==KeyEvent.ACTION_DOWN){
        if (System.currentTimeMillis()-exitTime>2000){
            toast(msg)
            exitTime = System.currentTimeMillis()
        }else{
            finish()
            if (exit){
                exitProcess(0)
            }

        }
        return true
    }
    return onKeyDown(keyCode, event)
}

suspend fun Context.getImageBitmapByUrl(url: String): Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(url)
        .allowHardware(false)
        .build()
    val result = (imageLoader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}

