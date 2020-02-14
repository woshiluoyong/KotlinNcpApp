package com.stephen.kotlin.demo.mvp.view.act

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.stephen.kotlin.demo.ExpandAdapter
import com.stephen.kotlin.demo.MainApplication
import com.stephen.kotlin.demo.R
import com.stephen.kotlin.demo.bean.AreaTree
import com.stephen.kotlin.demo.bean.Children
import com.stephen.kotlin.demo.bean.ChildrenX
import com.stephen.kotlin.demo.bean.MainData
import com.stephen.kotlin.demo.mvp.contract.MainContract
import com.stephen.kotlin.demo.mvp.presenter.MainPresenter
import com.stephen.kotlin.demo.mvp2.MvpActivity
import com.stephen.kotlin.demo.tool.ToolUtil
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import mvp.ljb.kt.act.BaseMvpAppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : BaseMvpAppCompatActivity<MainContract.IPresenter>() , MainContract.IView {
    private lateinit var mGroupList: ArrayList<Children>
    private lateinit var mChildrenList: ArrayList<ArrayList<ChildrenX>>
    private val internationalStr = StringBuffer()
    private val confirmAddRankStr = StringBuffer()
    private var isLoadDataOk: Boolean = false

    private lateinit var expandAdapter: ExpandAdapter

    private var circleProgressDialog: CircleProgressDialog? = null

    override fun registerPresenter() = MainPresenter::class.java

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        toolBar.inflateMenu(R.menu.toolbar)
        //toolBar.logo = ResourcesCompat.getDrawable(resources, R.drawable.tab_home_normal, null)

        drawerT.text = "${resources.getText(R.string.about_info)}\n\n\n\n\n${resources.getText(R.string.technology_info)}"

        toolBar.setOnMenuItemClickListener {
            when(it?.itemId){
                R.id.menuBtn -> ToolUtil.getInstance(this).whenConsume{ drawerId.openDrawer(GravityCompat.START) }
                R.id.goToBtn -> ToolUtil.getInstance(this).whenConsume {
                    /*val bundle = Bundle()
                    bundle.putSerializable(MvpActivity.ParamBase, mNewsList)*/
                    //goActivity(MvpActivity::class.java)//, bundle)
                    if(checkIsLoadOk())startActivity<MvpActivity>()
                }
                R.id.topInfoBtn -> ToolUtil.getInstance(this).whenConsume {
                    startActivity<TestAnkoActivity>()
                }
                R.id.upgradeBtn -> ToolUtil.getInstance(this).whenConsume {
                    alert("有新版本时在app启动时自动弹出来提升下载框，如果点击'下载'按钮后需要授权'存储'权限，请点击'允许'按钮，授权后会在下次app启动时点击'下载'按钮生效！","更新提醒"){
                        okButton {}
                    }.show()
                }
                else -> ToolUtil.getInstance(this).whenConsume { Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show() }
            }
        }

        refreshLy.setOnRefreshListener {
            it.finishRefresh()
            (getPresenter() as MainPresenter).getMainInfoData(false,"disease_h5")
        }

        emptyT.onClick {
            (getPresenter() as MainPresenter).getMainInfoData(true,"disease_h5")
        }

        refreshBtn.onClick {
            (getPresenter() as MainPresenter).getMainInfoData(true,"disease_h5")
        }

        about2Btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(!checkIsLoadOk())return
                alert(confirmAddRankStr, "确诊对比数据"){
                    okButton {}
                }.show()
            }
        })
        aboutBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(!checkIsLoadOk())return
                alert(internationalStr, "国际疫情数据"){
                    okButton {}
                }.show()
                /*val contries = listOf<String>("hdhd", "zdj", "dhhh")
                selector("Title", contries){ds, i ->
                    toast("=====>${contries[i]}")
                }*/
            }
        })
    }

    override fun initData() {
        mGroupList = ArrayList()
        mChildrenList = ArrayList()

        expandAdapter = ExpandAdapter(this, mGroupList, mChildrenList)
        with(expandV, {
            setAdapter(expandAdapter)
            setOnChildClickListener { p0, p1, p2, p3, p4 ->
                Toast.makeText(this@MainActivity, "Test", Toast.LENGTH_SHORT).show()
                true
            }
        })

        refreshLy.autoRefresh()
    }

    override fun showLoading() {
        /*Observable.empty<String>().subscribeOn(AndroidSchedulers.mainThread()).subscribe{

        }*/
        /*runBlocking {

        }*/
        val context = this
        /*GlobalScope.launch(Dispatchers.Main) {
            System.out.println("===================>协程执行线程：${Thread.currentThread().name}")
            if(null == circleProgressDialog)circleProgressDialog = CircleProgressDialog(context)
            circleProgressDialog!!.run {
                setText("卖命加载...")
                circleProgressDialog!!.setProgressColor(Color.parseColor("#ff0000"))
                circleProgressDialog!!.showDialog()
            }
        }*/
        doAsync {
            uiThread {
                System.out.println("===================>协程执行线程：${Thread.currentThread().name}")
                if(null == circleProgressDialog)circleProgressDialog = CircleProgressDialog(context)
                circleProgressDialog!!.run {
                    setText("卖命加载...")
                    setTextColor(Color.parseColor("#ffffff"))
                    circleProgressDialog!!.setProgressColor(Color.parseColor("#ff0000"))
                    circleProgressDialog!!.showDialog()
                }
            }
        }
    }

    override fun hideLoading() {
        /*Observable.empty<String>().subscribeOn(AndroidSchedulers.mainThread()).subscribe{

        }*/
        GlobalScope.async(Dispatchers.Main) {
            if(null != circleProgressDialog)circleProgressDialog!!.dismiss()
        }
    }

    override fun showMainInfoData(mainData: MainData?) {
        if(null != mainData){
            emptyT.visibility = View.GONE
            coordinatorLy.visibility = View.VISIBLE

            topDateT.text = "统计截止${mainData.lastUpdateTime}"

            val suspectVal = if(mainData.chinaDayAddList.size > 0) {mainData.chinaDayAddList[mainData.chinaDayAddList.size - 1].suspect ?: 0} else 0

            val data0Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.confirm else 0}")
            data0Str.setSpan(ForegroundColorSpan(Color.RED), data0Str.indexOf("+"), data0Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData0T.text = data0Str
            addData1T.text = "较上日+$suspectVal"
            val data2Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.heal else 0}")
            data2Str.setSpan(ForegroundColorSpan(Color.parseColor("#009352")), data2Str.indexOf("+"), data2Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData2T.text = data2Str
            addData3T.text = "较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.dead else 0}"

            curData0T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.confirm else 0}"
            curData1T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.suspect else 0}"
            curData2T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.heal else 0}"
            curData3T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.dead else 0}"

            mGroupList.clear()
            mChildrenList.clear()
            var mInternationalList: ArrayList<AreaTree> = ArrayList()
            internationalStr.delete(0, internationalStr.length)
            Observable.fromIterable(mainData.areaTree).concatMap{
                if(null == it.children)mInternationalList.add(it)
                Observable.fromIterable(it.children ?: listOf())
            }.filter{
                null != it.children && it.children.size > 0
            }.doOnComplete{
                //System.out.println("===================>doOnComplete:${mGroupList}")
                expandAdapter?.notifyDataSetChanged()

                Observable.fromIterable(mInternationalList).subscribe{
                    if(null != it)internationalStr.append("(${it.name})确诊:${it.total.confirm}人/疑似:${it.total.suspect}人/治愈:${it.total.heal}人/死亡:${it.total.dead}人\n\n")
                }
            }.subscribe{
                if(!TextUtils.isEmpty(it.name))mGroupList.add(it)
                if(null != it.children && it.children.size > 0){
                    val itemList: ArrayList<ChildrenX> = ArrayList()
                    Observable.fromIterable(it.children).doOnComplete{
                        mChildrenList.add(itemList)
                        //System.out.println("===================>doOnComplete2:${mChildrenList}")
                    }.subscribe{
                        itemList.add(it)
                    }
                }// end of if
            }

            confirmAddRankStr.delete(0, confirmAddRankStr.length)
            Observable.fromIterable(mainData.confirmAddRank).subscribe {
                if(null != it)confirmAddRankStr.append("(${it.name})昨天确诊:${it.yesterday}人/之后确诊:${it.before}人/增加比例:${it.addRate}%\n\n")
            }

            if(null != mainData.articleList){
                MainApplication.mNewsList.apply {
                    clear()
                    addAll(mainData.articleList)
                }
            }

            isLoadDataOk = true
        }else{
            loadMainDataError(null)
        }
    }

    override fun loadMainDataError(msg: String?) {
        isLoadDataOk = false
        if(null != msg && msg!!.isNotEmpty())showToast(msg)
        emptyT.visibility = View.VISIBLE
        coordinatorLy.visibility = View.GONE
    }

    fun checkIsLoadOk(): Boolean{
        if(!isLoadDataOk){
            toast("请先加载数据成功再点击!")
            return false
        }// end of if
        return true
    }

    override fun onBackPressed() {
        if(drawerId.isDrawerOpen(GravityCompat.START)){
            drawerId.closeDrawer(GravityCompat.START)
            return
        }// end of if
        super.onBackPressed()
    }
}
