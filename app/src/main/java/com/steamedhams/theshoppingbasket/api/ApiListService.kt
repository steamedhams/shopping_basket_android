package com.steamedhams.theshoppingbasket.api

import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItemsWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rx.Observable

/**
 * Created by richard on 16/02/17.
 */
interface ApiListService {

    @POST("v1_test/item")
    fun createListItem(@Body item : ShoppingListItem) : Observable<Void>

    @GET("v1_test/item")
    fun getList() : Observable<ShoppingListItemsWrapper>
}