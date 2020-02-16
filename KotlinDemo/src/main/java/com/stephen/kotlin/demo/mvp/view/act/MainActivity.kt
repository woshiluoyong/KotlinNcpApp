package com.stephen.kotlin.demo.mvp.view.act

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import cn.jiguang.analytics.android.api.BrowseEvent
import cn.jiguang.analytics.android.api.CountEvent
import cn.jiguang.analytics.android.api.JAnalyticsInterface
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

    override fun onStart() {
        super.onStart()
        JAnalyticsInterface.onPageStart(this, this.javaClass?.name)
    }

    override fun onStop() {
        super.onStop()
        JAnalyticsInterface.onPageEnd(this, this.javaClass?.name)
    }

    override fun registerPresenter() = MainPresenter::class.java

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        JAnalyticsInterface.setAnalyticsReportPeriod(applicationContext, 0)

        toolBar.inflateMenu(R.menu.toolbar)
        //toolBar.logo = ResourcesCompat.getDrawable(resources, R.drawable.tab_home_normal, null)

        drawerT.text = "${resources.getText(R.string.about_info)}\n\n\n\n\n${resources.getText(R.string.technology_info)}"

        toolBar.setOnMenuItemClickListener {
            when(it?.itemId){
                R.id.menuBtn -> ToolUtil.getInstance(this).whenConsume{
                    drawerId.openDrawer(GravityCompat.START)
                    JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickMenuBtn"))
                }
                R.id.goToBtn -> ToolUtil.getInstance(this).whenConsume {
                    /*val bundle = Bundle()
                    bundle.putSerializable(MvpActivity.ParamBase, mNewsList)*/
                    //goActivity(MvpActivity::class.java)//, bundle)
                    if(checkIsLoadOk())startActivity<MvpActivity>()
                    JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickNewsBtn"))
                }
                R.id.topInfoBtn -> ToolUtil.getInstance(this).whenConsume {
                    startActivity<TestAnkoActivity>()
                    JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickAnkoTestBtn"))
                }
                R.id.upgradeBtn -> ToolUtil.getInstance(this).whenConsume {
                    alert("有新版本时在app启动时自动弹出来提升下载框，如果点击'下载'按钮后需要授权'存储'权限，请点击'允许'按钮，授权后会在下次app启动时点击'下载'按钮生效！","更新提醒"){
                        okButton {}
                    }.show()
                    JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickUpgradeBtn"))
                }
                else -> ToolUtil.getInstance(this).whenConsume { Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show() }
            }
        }

        refreshLy.setOnRefreshListener {
            it.finishRefresh()
            (getPresenter() as MainPresenter).getMainInfoData(false,"disease_h5")
            JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickRefreshLy"))
        }

        emptyT.onClick {
            (getPresenter() as MainPresenter).getMainInfoData(true,"disease_h5")
            JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickEmptyReload"))
        }

        refreshBtn.onClick {
            (getPresenter() as MainPresenter).getMainInfoData(true,"disease_h5")
            JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickRefreshBtn"))
        }

        about2Btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(!checkIsLoadOk())return
                alert(confirmAddRankStr, "确诊新增数据"){
                    okButton {}
                }.show()
                JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickConfirmAddRank"))
            }
        })
        aboutBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(!checkIsLoadOk())return
                alert(internationalStr, "国际疫情数据"){
                    okButton {}
                }.show()
                JAnalyticsInterface.onEvent(applicationContext, CountEvent("ClickInternational"))
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
            setOnGroupClickListener { expandableListView, view, i, l ->
                val clickVal = view?.tag?.toString() ?: "ClickGroup"
                Toast.makeText(this@MainActivity, clickVal, Toast.LENGTH_SHORT).show()
                val countEvent = CountEvent("ClickMainGroup")
                countEvent.addKeyValue("data", clickVal)
                JAnalyticsInterface.onEvent(applicationContext, countEvent)
                false
            }
            setOnChildClickListener { expandableListView, view, p2, p3, p4 ->
                val clickVal = view?.tag?.toString() ?: "ClickChild"
                Toast.makeText(this@MainActivity, clickVal, Toast.LENGTH_SHORT).show()
                val countEvent = CountEvent("ClickMainChild")
                countEvent.addKeyValue("data", clickVal)
                JAnalyticsInterface.onEvent(applicationContext, countEvent)
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

            val suspectVal = if(mainData.chinaDayAddList.isNotEmpty()) {mainData.chinaDayAddList[mainData.chinaDayAddList.size - 1].suspect ?: 0} else 0

            val data0Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.confirm else 0}")
            data0Str.setSpan(ForegroundColorSpan(Color.parseColor("#d6001d")), data0Str.indexOf("+"), data0Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData0T.text = data0Str
            addData1T.text = "较上日+$suspectVal"
            val data2Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.heal else 0}")
            data2Str.setSpan(ForegroundColorSpan(Color.parseColor("#009352")), data2Str.indexOf("+"), data2Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData2T.text = data2Str
            addData3T.text = "较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.dead else 0}"
            val data4Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.nowConfirm else 0}")
            data4Str.setSpan(ForegroundColorSpan(Color.parseColor("#ff3753")), data4Str.indexOf("+"), data4Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData4T.text = data4Str
            val data5Str = SpannableString("较上日+${if(null != mainData.chinaAdd) mainData.chinaAdd.nowSevere else 0}")
            data5Str.setSpan(ForegroundColorSpan(Color.parseColor("#d50080")), data5Str.indexOf("+"), data5Str.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            addData5T.text = data5Str

            curData0T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.confirm else 0}"
            curData1T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.suspect else 0}"
            curData2T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.heal else 0}"
            curData3T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.dead else 0}"
            curData4T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.nowConfirm else 0}"
            curData5T.text = "${if(null != mainData.chinaTotal) mainData.chinaTotal.nowSevere else 0}"

            mGroupList.clear()
            mChildrenList.clear()
            var mInternationalList: ArrayList<AreaTree> = ArrayList()
            internationalStr.delete(0, internationalStr.length)
            Observable.fromIterable(mainData.areaTree).concatMap{
                if(null == it.children)mInternationalList.add(it)
                Observable.fromIterable(it.children ?: listOf())
            }.filter{
                null != it.children && it.children.isNotEmpty()
            }.doOnComplete{
                //System.out.println("===================>doOnComplete:${mGroupList}")
                expandAdapter?.notifyDataSetChanged()

                if(null != mInternationalList && mInternationalList.isNotEmpty())Observable.fromIterable(mInternationalList).subscribe{
                    if(null != it)internationalStr.append("(${it.name})确诊:${it.total.confirm}人/疑似:${it.total.suspect}人/治愈:${it.total.heal}人/死亡:${it.total.dead}人\n\n")
                }
            }.subscribe{
                if(!TextUtils.isEmpty(it.name))mGroupList.add(it)
                if(null != it.children && it.children.isNotEmpty()){
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
            if(null != mainData.chinaDayAddList && mainData.chinaDayAddList.isNotEmpty())Observable.fromIterable(mainData.chinaDayAddList).subscribe {
                if(null != it)confirmAddRankStr.append("(日期:${it.date}) 确诊:${it.confirm}人/疑似:${it.suspect}人/治愈:${it.heal}/治愈比例:${it.healRate}/死亡:${it.dead}%/死亡比例:${it.deadRate}%\n\n")
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
