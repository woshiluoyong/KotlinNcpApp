package com.stephen.kotlin.demo.mvp2

class MsgPresenter(val mView: MsgContract.IView) : MsgContract.IMsgPresenter {
    private val msgModel = MsgModel()

    override fun saveMsgToModel(msgStr: String) {
        msgModel.saveMsg(MsgBean(msgStr))
        mView.onSaveMsgSuccess()
    }

    override fun loadMsgFromShow() {
        return mView.getShowMsgInfo("保存的信息:${msgModel.loadMsg()?.msgStr!!}")
    }
}