package com.stephen.kotlin.demo.mvp2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stephen.kotlin.demo.MainApplication
import com.stephen.kotlin.demo.NewsAdapter
import com.stephen.kotlin.demo.R
import com.stephen.kotlin.demo.bean.Article
import kotlinx.android.synthetic.main.activity_main.coordinatorLy
import kotlinx.android.synthetic.main.activity_mvp.*

class MvpActivity : AppCompatActivity(), MsgContract.IView {
    companion object{
        const val ParamBase: String = "ParamBase"
    }

    private lateinit var mMsgPresenter: MsgPresenter

    //private lateinit var mNewsList: ArrayList<Article>

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)

        mMsgPresenter = MsgPresenter(this)

        //mNewsList = intent.getSerializableExtra(ParamBase) as ArrayList<Article>

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        newsListV.layoutManager = linearLayoutManager
        newsAdapter = NewsAdapter(MainApplication.mNewsList, this, R.layout.item_news_item)
        newsListV.adapter = newsAdapter

        floatBtn.setOnClickListener {
            Snackbar.make(coordinatorLy, "MvpSave", Snackbar.LENGTH_LONG).setAction("Save") {
                mMsgPresenter.saveMsgToModel(resources.getString(R.string.about_info))
            }.show()
        }

        float2Btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Snackbar.make(coordinatorLy, "MvpLoad", Snackbar.LENGTH_INDEFINITE).setAction("Load") {
                    mMsgPresenter.loadMsgFromShow()
                }.show()
            }
        })
    }

    override fun onSaveMsgSuccess() {
        Toast.makeText(this, "saveMsgSuccess", Toast.LENGTH_SHORT).show()
    }

    override fun getShowMsgInfo(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
