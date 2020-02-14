package com.stephen.kotlin.demo.mvp

import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
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