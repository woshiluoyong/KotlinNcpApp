package com.stephen.kotlin.demo.mvp.presenter

import mvp.ljb.kt.presenter.BaseMvpPresenter
import com.stephen.kotlin.demo.mvp.contract.MainContract
import com.stephen.kotlin.demo.mvp.model.MainModel

/**
 * @Author Kotlin MVP Plugin
 * @Date 2020/02/11
 * @Description input description
 **/
class MainPresenter : BaseMvpPresenter<MainContract.IView, MainContract.IModel>(), MainContract.IPresenter{

    override fun registerModel() = MainModel::class.java

    fun getMainInfoData(isLoading: Boolean, httpName: String) {
        (getModel() as MainModel).getMainShowData(isLoading, httpName){
            setShowLoading {
                getMvpView().showLoading()
            }
            setHideLoading {
                getMvpView().hideLoading()
            }
            setOnSuccess {
                getMvpView().showMainInfoData(it)
            }
            setOnFailure {
                getMvpView().loadMainDataError(it)
            }
        }
    }
}
