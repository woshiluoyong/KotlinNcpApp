package com.stephen.kotlin.demo

import android.content.Context
import android.view.View
import com.caveman.timeaxis.adapter.TimeAxisAdapter
import com.caveman.timeaxis.holder.CommonViewHolder
import com.caveman.timeaxis.weight.TimeAxisView
import com.stephen.kotlin.demo.bean.Article
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast


class NewsAdapter : TimeAxisAdapter<Article>{
    var context: Context? = null
    var dataList: List<Article>? = null

    constructor(dataList: List<Article>, context: Context): super(dataList, context){
        this.context = context
        this.dataList = dataList
    }

    constructor(dataList: List<Article>, context: Context, resId: Int): super(dataList, context, resId){
        this.context = context
        this.dataList = dataList
    }

    override fun initView(holder: CommonViewHolder?, position: Int) {
        val article: Article = mDataSource[position]

        val mTimeAxisView: TimeAxisView = holder!!.getView(R.id.lineT)
        mTimeAxisView.setBigText(article.publish_time)
        mTimeAxisView.setSmallText(context?.resources?.getString(R.string.app_name))
        mTimeAxisView.setBigTextSize(25f)
        if (article.can_use === 1) {//根据状态设置圆圈样式
            mTimeAxisView.setCircleShape(TimeAxisView.SOLID_CIRCLE)
        } else if (article.can_use === 0) {
            mTimeAxisView.setCircleShape(TimeAxisView.CENTER_CIRCLE)
        } else {
            mTimeAxisView.setCircleShape(TimeAxisView.HOLLOW_CIRCLE)
        }

        holder.setText(R.id.newsTitleT, article.title)
        holder.setText(R.id.newsContentT, article.desc)
        holder.setText(R.id.newsFromT, "来源:${article.media}")

        holder.getView<View>(R.id.infoLy).setOnClickListener {
            context?.toast("正在打开网页中...")
            context?.browse(article.url)
        }
    }
}