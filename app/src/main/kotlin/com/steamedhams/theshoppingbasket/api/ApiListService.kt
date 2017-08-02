package com.steamedhams.theshoppingbasket.api

import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItemsWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import rx.Observable

/**
 * Created by richard on 16/02/17.
 */
interface ApiListService {

    @GET("/list_items")
    fun getListItems() : Observable<List<ShoppingListItem>>

    @POST("/list_items")
    fun createListItem(@Body item : ShoppingListItem) : Observable<ShoppingListItem>

    @GET("/lists")
    fun getLists() : Observable<List<ShoppingList>>

    @POST("/lists")
    fun createList(@Body list : ShoppingList) : Observable<ShoppingList>
}