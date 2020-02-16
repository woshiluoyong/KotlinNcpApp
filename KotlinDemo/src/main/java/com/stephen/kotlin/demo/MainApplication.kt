package com.stephen.kotlin.demo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import cn.jiguang.analytics.android.api.JAnalyticsInterface
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
        JAnalyticsInterface.setDebugMode(true)
        JAnalyticsInterface.init(this)
        //Bmob.initialize(this, "aeff0d31df886a4efbf920c5aef4f054")
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}