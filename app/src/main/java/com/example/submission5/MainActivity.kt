package com.example.submission5

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission5.mainmodel.adapter.MainAdapter
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.example.submission5.mainmodel.viewmodel.SearchUser
import com.example.submission5.mainmodel.viewmodel.UserViewModel
import com.example.submission5.ui.detail.DetailUserActivity
import com.example.submission5.ui.favorite.FavoriteActivity
import com.example.submission5.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter: MainAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var searchUser: SearchUser
    var displayList = ArrayList<UserParcelable>()
    var displayFilterList = ArrayList<UserParcelable>()
    var tempData = ArrayList<UserParcelable>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = View.VISIBLE

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(5000)
            progressBar.visibility = View.INVISIBLE
            if (!verifyAvailableNetwork(this@MainActivity) && mainAdapter.itemCount == 0){
                tv_information.text = "Check your internet connection!"
            }
            else if(mainAdapter.itemCount == 0) {
                tv_information.text = "Access is limited!\nPlease try again in few minutes..."
            }
        }

        mainAdapter = MainAdapter()
        mainAdapter.notifyDataSetChanged()

        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = mainAdapter
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        searchUser = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchUser::class.java)
        tv_information.text = "Loading..."

        userViewModel.setUser()
        userViewModel.getUser().observe(this, Observer { UserParcelable ->
            progressBar.visibility = View.INVISIBLE
            if (UserParcelable != null){
                val resId = R.anim.layout_animation_slide_from_bottom
                val animation = AnimationUtils.loadLayoutAnimation(this, resId)
                animation.delay = 0.20f
                animation.order = LayoutAnimationController.ORDER_NORMAL

                recycleview.layoutAnimation = animation
                tv_information.text = ""
                mainAdapter.setData(UserParcelable)
                tempData.addAll(UserParcelable)
            }

        })
        mainAdapter.setOnItemClickCallBack(object: MainAdapter.OnItemClickCallBack{
            override fun onItemClicked(userParcelable: UserParcelable) {
                var intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_DATA_PLUS, userParcelable)
                startActivity(intent)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)

        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                tv_information.text = "Loading..."
                displayList.clear()
                mainAdapter.setData(displayList)
                recycleview.adapter?.notifyDataSetChanged()
                searchUser.setSearchUser(query)

                searchUser.getSearchUser().observe(this@MainActivity, Observer {
                        UserParcelable ->
                    if (UserParcelable != null) {
                        displayList.addAll(UserParcelable)
                        displayFilterList.addAll(UserParcelable)
                        tv_information.text = ""
                        progressBar.visibility = View.INVISIBLE
                        if(UserParcelable.size == 0){
                            tv_information.text = "Data Not Found"
                        }
                    }
                    if (query.isNotEmpty()) {
                        displayList.clear()
                        val search = query.toLowerCase()
                        displayFilterList.forEach {
                            if (it.username!!.toLowerCase().contains(search)) {
                                displayList.add(it)
                            }
                        }
                        displayFilterList.clear()
                    } else {
                        displayList.clear()
                        progressBar.visibility = View.INVISIBLE
                        displayList.addAll(tempData)
                    }
                    mainAdapter.setData(displayList)
                    recycleview.adapter?.notifyDataSetChanged()
                })
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    displayList.clear()
                    tv_information.text = ""
                    progressBar.visibility = View.INVISIBLE
                    displayList.addAll(tempData)
                    mainAdapter.setData(displayList)
                    recycleview.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRestart() {
        super.onRestart()
        recycleview.startLayoutAnimation()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.menu_favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.refresh -> {
                progressBar.visibility = View.VISIBLE
                tv_information.text = "Loading..."
                GlobalScope.launch(context = Dispatchers.Main) {
                    delay(5000)
                    progressBar.visibility = View.INVISIBLE
                    if (!verifyAvailableNetwork(this@MainActivity) && mainAdapter.itemCount == 0){
                        tv_information.text = "Check your internet connection!"
                    }
                    else if(mainAdapter.itemCount == 0) {
                        tv_information.text = "Access is limited!\nPlease try again in few minutes..."
                    }
                }
                userViewModel.setUser()
                userViewModel.getUser().observe(this, Observer { UserParcelable ->
                    progressBar.visibility = View.INVISIBLE
                    if (UserParcelable != null){
                        tv_information.text = ""
                        mainAdapter.setData(UserParcelable)
                        tempData.addAll(UserParcelable)
                        recycleview.adapter?.notifyDataSetChanged()

                        if(UserParcelable.size == 0){
                            tv_information.text = "Check Your Internet Connection!"
                        }
                    }

                })
            }
        }

        return super.onOptionsItemSelected(item)
    }
    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager= activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}