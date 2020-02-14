package com.stephen.kotlin.demo.mvp

interface OnCallBack<T> {
    fun showLoading()
    fun hideLoading()
    fun onSuccess(data: T)
    fun onFailure(message: String?)
}

class OnCallbackImpl<T> : OnCallBack<T>{//高阶函数多方法回调
    private lateinit var showLoadingFun: () -> Unit
    private lateinit var hideLoadingFun: () -> Unit
    private lateinit var onSuccessFun: (T) -> Unit
    private lateinit var onFailureFun: (String?) -> Unit

    fun setShowLoading(listener: () -> Unit){
        this.showLoadingFun = listener
    }

    override fun showLoading() {
        this.showLoadingFun.invoke()
    }

    fun setHideLoading(listener: () -> Unit){
        this.hideLoadingFun = listener
    }

    override fun hideLoading() {
        this.hideLoadingFun.invoke()
    }

    fun setOnSuccess(listener: (T) -> Unit) {
        this.onSuccessFun = listener
    }

    override fun onSuccess(data: T) {
        this.onSuccessFun.invoke(data)
    }

    fun setOnFailure(listener: (String?) -> Unit) {
        this.onFailureFun = listener
    }

    override fun onFailure(message: String?) {
        this.onFailureFun.invoke(message)
    }
}