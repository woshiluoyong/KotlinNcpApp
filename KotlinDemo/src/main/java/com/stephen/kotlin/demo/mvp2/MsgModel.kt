package com.stephen.kotlin.demo.mvp2

class MsgModel : MsgContract.IModel {
    private var msgBean: MsgBean? = MsgBean("DefaultEmptyInfo")

    override fun saveMsg(msgBean: MsgBean?) {
        this.msgBean = msgBean
    }

    override fun loadMsg(): MsgBean? {
        return msgBean
    }
}