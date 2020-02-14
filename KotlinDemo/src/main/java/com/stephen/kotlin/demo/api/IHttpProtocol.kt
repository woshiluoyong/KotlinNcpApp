package com.stephen.kotlin.demo.api

import com.stephen.kotlin.demo.bean.MainBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IHttpProtocol {
    @GET("g2/getOnsInfo")
    fun getMainData(@Query("name") name: String): Observable<MainBean>
}