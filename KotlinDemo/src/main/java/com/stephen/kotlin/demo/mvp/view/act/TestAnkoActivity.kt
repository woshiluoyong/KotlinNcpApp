package com.stephen.kotlin.demo.mvp.view.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.marginLeft
import com.stephen.kotlin.demo.R
import com.stephen.kotlin.demo.mvp.AnkoActivityUi
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class TestAnkoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_test_anko)
        AnkoActivityUi().setContentView(this)

        bindShowToastInfo()
    }

    fun bindShowToastInfo(){
        find<View>(R.id.relativeLayoutTextId).onClick {
            toast("toastShowInfo")
        }
    }
}
