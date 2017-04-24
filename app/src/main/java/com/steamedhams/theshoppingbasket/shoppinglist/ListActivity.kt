package com.steamedhams.theshoppingbasket.shoppinglist

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.steamedhams.theshoppingbasket.R
import com.steamedhams.theshoppingbasket.ShoppingBasket
import com.steamedhams.theshoppingbasket.api.ApiListService
import com.steamedhams.theshoppingbasket.data.model.ShoppingList
import com.steamedhams.theshoppingbasket.data.model.ShoppingListItem
import com.steamedhams.theshoppingbasket.data.realm.RealmDelegate
import com.steamedhams.theshoppingbasket.databinding.ActivityListBinding
import javax.inject.Inject


/**
 * Activity which will host a Shopping list, handling displaying and interacting with that list
 * including adding, editing and removing items from the list
 * <p>
 * Created by richard on 13/02/17.
 */
class ListActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val RC_SIGN_IN = 9001

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var binding : ActivityListBinding
    private lateinit var adapter : ShoppingListAdapter

    private lateinit var googleApiClient: GoogleApiClient

    @Inject lateinit var listService : ApiListService
    @Inject lateinit var realmDelegate : RealmDelegate

    private var currentList : String = ""
        set(value) {
            field = value
            adapter.viewId = value
            binding.listToolbar.title = realmDelegate.getList(value).title
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
        binding.listToolbar.setTitle(R.string.list_activity_title)

        ShoppingBasket.netComponent.inject(this)

        setSupportActionBar(binding.listToolbar)
        binding.listToolbar.setNavigationOnClickListener { toggleNavDrawer() }

        NavDrawViewHolder(binding.navDrawer, this,
                { viewId ->
                        currentList = viewId
                        binding.navDrawerLayout.closeDrawers()
                })

        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        binding.navDrawer.signinPanel.googleSigninButton.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        setUpShoppingList()
    }

    override fun onResume() {
        super.onResume()
        updateUserInfo()
//        if (realmDelegate.getLists().isNotEmpty()) {
//            currentList = realmDelegate.getLists()[0].UUID
//        } else {
//            NewItemDialog({viewTitle -> onListCreated(viewTitle) })
//                    .show(fragmentManager, NewItemDialog.getTag())
//        }
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

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        Log.d(this.javaClass.simpleName, "firebaseAuthWithGoogle:" + acct?.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(this.javaClass.simpleName, "signInWithCredential:success")
                            updateUserInfo()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(this.javaClass.simpleName, "signInWithCredential:failure", task.exception)
                            Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            updateUserInfo()
                        }

                    }
                })
    }

    private fun updateUserInfo() {
        if (firebaseAuth.currentUser != null) {
               
        }
    }

    private fun onListCreated(viewTitle: String) {
        val list = ShoppingList(viewTitle)
        realmDelegate.addList(list)
        currentList = list.UUID
    }

    private fun toggleNavDrawer() {
        if (binding.navDrawerLayout.isDrawerOpen(Gravity.START)) {
            binding.navDrawerLayout.closeDrawers()
        } else {
            binding.navDrawerLayout.openDrawer(Gravity.START)
        }
    }

    private fun setUpShoppingList() {
        val shoppingList = binding.shoppingList

        shoppingList.layoutManager = LinearLayoutManager(this)
        shoppingList.itemAnimator = DefaultItemAnimator()
        adapter = ShoppingListAdapter()
        shoppingList.adapter = adapter

        binding.listActivityNewItemFab.setOnClickListener {
            NewItemDialog({string -> onShoppingListItemCreated(string)})
                    .show(fragmentManager, NewItemDialog.getTag())
        }
    }

    fun onShoppingListItemCreated(itemName: String) {
        val listItem = ShoppingListItem(currentList, itemName)
        adapter.addItem(listItem)
    }

}
