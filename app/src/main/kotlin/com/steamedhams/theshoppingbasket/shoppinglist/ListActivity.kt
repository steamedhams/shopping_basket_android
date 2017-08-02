package com.steamedhams.theshoppingbasket.shoppinglist

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.api.ApiListService
import com.steamedhams.theshoppingbasket.api.helper.IoToMainThreadTransformer
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.room.BasketDatabase
import kotlinx.android.synthetic.main.activity_list.*
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Activity which will host a Shopping list, handling displaying and interacting with that list
 * including adding, editing and removing items from the list
 * <p>
 * Created by richard on 13/02/17.
 */
class ListActivity : LifecycleActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val RC_SIGN_IN = 9001

    val LOG = Logger.getLogger(this.javaClass.name)

//    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapter: ShoppingListAdapter

    lateinit var navDrawerViewHolder: NavDrawViewHolder
    private lateinit var googleApiClient: GoogleApiClient

    @Inject lateinit var listService: ApiListService
    @Inject lateinit var database: BasketDatabase

    private var currentList: ShoppingList? = null
        set(value) {
            field = value
            adapter.viewId = value?.id.toString()
        }

    lateinit var listLiveData: LiveData<MutableList<ShoppingListItem>>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listToolbar.setTitle(R.string.list_activity_title)

        ShoppingBasket.netComponent.inject(this)

        setActionBar(listToolbar)
        listToolbar.setNavigationOnClickListener { toggleNavDrawer() }

        navDrawerViewHolder = NavDrawViewHolder(navDrawer, this,
                { listId ->
                    Single.fromCallable {
                                currentList = database.shoppingListDao().getList(listId)
                            }
                            .map { database.shoppingListItemDao().getAllShoppingListItems().value }//ForList(currentList!!.id).value }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({shoppingListItems ->
                                setItemsToDisplay(shoppingListItems)
                                navDrawerLayout.closeDrawers()
                            })
                })

//        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

//        // Build a GoogleApiClient with access to the Google Sign-In API and the
//        // options specified by gso.
//        googleApiClient = GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build()
//
//        navDrawer.googleSignInButton.setOnClickListener {
//            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }

        setUpShoppingList()
        updateCurrentList()
        getData()
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
    }

    private fun updateCurrentList() {
//        listLiveData = database.shoppingListItemDao().getAllShoppingListItemsForList(currentList!!.id)
        listLiveData = database.shoppingListItemDao().getAllShoppingListItems()
        listLiveData.observe(this, Observer { listItems ->
            setItemsToDisplay(listItems)
        })
    }

    private fun setItemsToDisplay(listItems: MutableList<ShoppingListItem>?) {
        adapter.clearItems()
        listItems?.forEach { listItem ->
            if (listItem.list_id == currentList?.id) adapter.addItem(listItem)
            LOG.info(listItem.toString())
        }
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun getData() {
        listService.getLists()
                .doOnNext { shoppingLists ->
                    shoppingLists.forEach { shoppingList ->
                        LOG.info("ShoppingList: " + shoppingList.toString())
                        database.shoppingListDao().insert(shoppingList)
                    }
                    currentList = shoppingLists[0]
                }
                .flatMap { listService.getListItems() }
                .doOnNext { shoppingListItems ->
                    shoppingListItems.forEach { shoppingListItem ->
                        database.shoppingListItemDao().insert(shoppingListItem)
                    }
                }
                .compose(IoToMainThreadTransformer())
                .doOnNext {
                    listToolbar.title = currentList?.title
                    setItemsToDisplay(database.shoppingListItemDao().getAllShoppingListItemsForList(currentList!!.id).value)
                }
                .subscribe({}, { throwable -> LOG.log(Level.WARNING, "Error fetching shopping lists", throwable) })
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(this.javaClass.simpleName, "firebaseAuthWithGoogle:" + acct?.id!!)

//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
//                    override fun onComplete(task: Task<AuthResult>) {
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(this.javaClass.simpleName, "signInWithCredential:success")
//                            updateUserInfo()
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(this.javaClass.simpleName, "signInWithCredential:failure", task.exception)
//                            Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                            updateUserInfo()
//                        }
//
//                    }
//                })
    }

    private fun updateUserInfo() {
//        if (firebaseAuth.currentUser != null) {
//
//        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun toggleNavDrawer() {
        if (navDrawerLayout.isDrawerOpen(Gravity.START)) {
            navDrawerLayout.closeDrawers()
        } else {
            navDrawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun setUpShoppingList() {
        shoppingList.layoutManager = LinearLayoutManager(this)
        shoppingList.itemAnimator = DefaultItemAnimator()
        adapter = ShoppingListAdapter()
        shoppingList.adapter = adapter

        listActivityNewItemFab.setOnClickListener {
            NewItemDialog({ string -> onShoppingListItemCreated(string) })
                    .show(fragmentManager, NewItemDialog.getTag())
        }
    }

    fun onShoppingListItemCreated(itemName: String) {
        LOG.info("CurrentList ID = " + currentList!!.id)
        LOG.info("CurrentList = " + currentList)
        val listItem = ShoppingListItem(itemName, currentList!!.id)
        listService.createListItem(listItem)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { item -> database.shoppingListItemDao().insert(item) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    item ->
                    LOG.info(item.toString())
                }, {
                    throwable ->
                    LOG.log(Level.WARNING, "Fail to add list item", throwable)
                })
    }

}
