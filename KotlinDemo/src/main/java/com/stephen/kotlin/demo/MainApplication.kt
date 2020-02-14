package com.stephen.kotlin.demo

import android.app.Application
import com.pgyersdk.update.PgyUpdateManager
import com.stephen.kotlin.demo.bean.Article
import net.ljb.kt.HttpConfig

class MainApplication : Application(){
    companion object{
        var mNewsList: ArrayList<Article> = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
        HttpConfig.init("https://view.inews.qq.com/")
        PgyUpdateManager.Builder().setForced(true).setUserCanRetry(true).register()
        //Bmob.initialize(this, "aeff0d31df886a4efbf920c5aef4f054")
    }
}