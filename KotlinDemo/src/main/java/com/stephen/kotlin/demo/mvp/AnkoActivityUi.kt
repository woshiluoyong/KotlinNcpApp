package com.stephen.kotlin.demo.mvp

import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import cn.jiguang.analytics.android.api.CountEvent
import cn.jiguang.analytics.android.api.JAnalyticsInterface
import com.stephen.kotlin.demo.R
import com.stephen.kotlin.demo.mvp.view.act.TestAnkoActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.textChangedListener

class AnkoActivityUi : AnkoComponent<TestAnkoActivity>{
    override fun createView(ui: AnkoContext<TestAnkoActivity>) = with(ui){
        verticalLayout {
            textView("AnkoText") {
                textSize = sp(20).toFloat()
                textColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
                onClick {
                    toast("click:$text")
                    val countEvent = CountEvent("ClickAnkoText")
                    countEvent.addKeyValue("data", "$text")
                    JAnalyticsInterface.onEvent(context, countEvent)
                }
            }
            val editE = editText("AnkoInfo"){
                textChangedListener {
                    onTextChanged { str, start, before, end ->
                        System.out.println("============onTextChanged=====${text}==>")
                    }
                }
            }
            button("AnkoBtn"){
                onClick {
                    toast("click:${editE.text}")
                    val countEvent = CountEvent("ClickAnkoBtn")
                    countEvent.addKeyValue("data", "${editE.text}")
                    JAnalyticsInterface.onEvent(context, countEvent)
                }
            }.lparams {
                margin = dip(10)
            }

            frameLayout {
                textView {
                    text = "frameLayoutText1"
                }
                textView {
                    text = "frameLayoutText2"
                }.lparams {
                    topMargin = dip(15)
                }
            }.lparams {
                topMargin = dip(25)
            }

            relativeLayout {
                textView {
                    text = "relativeLayoutText1-click"
                    id = R.id.relativeLayoutTextId
                }.lparams {
                    addRule(RelativeLayout.CENTER_IN_PARENT)
                }
                textView {
                    text = "relativeLayoutText2"
                }.lparams {
                    addRule(RelativeLayout.BELOW, R.id.relativeLayoutTextId)
                }
            }.lparams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                topMargin = dip(25)
            }
        }
    }
}