package com.stephen.kotlin.demo.mvp2

interface MsgContract {
    interface IModel {
        fun saveMsg(msgBean: MsgBean?)
        fun loadMsg(): MsgBean?
    }

    interface IMsgPresenter {
        fun saveMsgToModel(msgStr: String)
        fun loadMsgFromShow()
    }

    interface IView {
        fun onSaveMsgSuccess()
        fun getShowMsgInfo(msg: String)
    }
}