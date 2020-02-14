package com.stephen.kotlin.demo.mvp.contract

import com.stephen.kotlin.demo.bean.MainData
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract
import mvp.ljb.kt.contract.IModelContract

/**
 * @Author Kotlin MVP Plugin
 * @Date 2020/02/11
 * @Description input description
 **/
interface MainContract {

    interface IView : IViewContract{
        fun showLoading()
        fun hideLoading()
        fun showMainInfoData(mainData: MainData?)
        fun loadMainDataError(msg: String?)
    }

    interface IPresenter : IPresenterContract

    interface IModel : IModelContract
}
