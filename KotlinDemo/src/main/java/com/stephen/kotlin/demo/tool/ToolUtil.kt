package com.stephen.kotlin.demo.tool

import android.content.Context

class ToolUtil private constructor(private val context: Context){
    companion object{
        @Volatile private var instance: ToolUtil? = null

        fun getInstance(context: Context) = instance ?: synchronized(this){
            instance ?: ToolUtil(context).also { instance = it }
        }
    }

    inline fun whenConsume(f: () -> Unit): Boolean{
        f()
        return true
    }

    fun checkPlusOrMinus(numVal: Int): String{
        return "${if(numVal < 0) "" else "+"}$numVal"
    }
}