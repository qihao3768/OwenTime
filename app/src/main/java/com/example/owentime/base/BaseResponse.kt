package com.example.owentime.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("errorCode")
    val errorCode:Int=0,
    @SerialName("errorMsg")
    val errorMsg:String="",
    @SerialName("data")
    val data:T
    )
