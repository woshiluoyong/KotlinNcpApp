package  com.stephen.kotlin.demo.mvp.model

import android.text.TextUtils
import com.google.gson.Gson
import com.stephen.kotlin.demo.api.IHttpProtocol
import com.stephen.kotlin.demo.bean.MainData
import com.stephen.kotlin.demo.mvp.OnCallbackImpl
import com.stephen.kotlin.demo.mvp.contract.MainContract
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.model.BaseModel
import net.ljb.kt.client.HttpFactory

/**
 * @Author Kotlin MVP Plugin
 * @Date 2020/02/11
 * @Description input description
 **/
class MainModel : BaseModel(), MainContract.IModel {
    lateinit var callListener: OnCallbackImpl<MainData>

    private fun isSetListener() = ::callListener.isInitialized

    fun getMainShowData(isLoading: Boolean, httpName: String, f: OnCallbackImpl<MainData>.() -> Unit) {
        val call = OnCallbackImpl<MainData>()
        call.f()
        this.callListener = call

        HttpFactory.getProtocol(isStrClient = false, clazz = IHttpProtocol::class.java).getMainData(httpName)
            .map {
                if(null != it && 0 == it.ret){
                    Gson().fromJson<MainData>(it.data, MainData::class.java)
                }else{
                    null
                }
            }
            .doOnSubscribe {
                System.out.println("===================>网络请求开始！")
                if(isLoading && isSetListener())callListener.showLoading()
            }
            .doFinally {
                if(isLoading && isSetListener())callListener.hideLoading()
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    System.out.println("===================>网络请求完成！")
                    if(isSetListener())callListener.onSuccess(it!!)
                },
                {
                    System.out.println("=============网络请求处理异常======>$it")
                    if(isSetListener())callListener.onFailure(if(null != it && !TextUtils.isEmpty(it.message)) it.message else "返回数据异常!")
                }
            )
    }


}