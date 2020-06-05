package com.glavesoft.pawnuser.model

data class PwHttpResult<T>(
    val code: String,
    val data: T,
    val message: String,
    val success: Boolean,
    val time: Long
)