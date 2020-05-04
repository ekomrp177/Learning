package com.example.myfavoriteuser

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfavoriteuser.adapter.FavoriteAdapter
import com.example.myfavoriteuser.room.CursorHelper
import com.example.myfavoriteuser.room.UserParcelable
import com.example.myfavoriteuser.room.UserParcelable.Companion.CONTENT_URI
import com.example.myfavoriteuser.ui.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onRestart() {
        super.onRestart()
        favoriteAdapter = FavoriteAdapter()
        if (!verifyAvailableNetwork(this@MainActivity)){
            Toast.makeText(this, "Check your internet connection!", Toast.LENGTH_LONG).show()
        }
        GlobalScope.launch(context = Dispatchers.Main) {
            progressBar.visibility = View.INVISIBLE
            val resId = R.anim.layout_animation_slide_from_bottom
            val animation = AnimationUtils.loadLayoutAnimation(applicationContext, resId)
            animation.delay = 0.20f
            animation.order = LayoutAnimationController.ORDER_NORMAL
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)

            favoriteAdapter.setData(CursorHelper.mapCursorToArrayList(cursor))
            favoriteAdapter.notifyDataSetChanged()
            recycleview.layoutAnimation = animation
            favoriteAdapter.setData(CursorHelper.mapCursorToArrayList(cursor))

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = View.VISIBLE
        tv_information.text = "Loading..."

        if (!verifyAvailableNetwork(this@MainActivity)){
            Toast.makeText(this, "Check your internet connection!", Toast.LENGTH_LONG).show()
        }
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(1000)
            progressBar.visibility = View.INVISIBLE
            val resId = R.anim.layout_animation_slide_from_bottom
            val animation = AnimationUtils.loadLayoutAnimation(applicationContext, resId)
            animation.delay = 0.20f
            animation.order = LayoutAnimationController.ORDER_NORMAL
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)

            recycleview.layoutAnimation = animation
            favoriteAdapter.setData(CursorHelper.mapCursorToArrayList(cursor))
            if (favoriteAdapter.itemCount == 0){
                tv_information.text = "Don't have favorite user"
            }else{

                tv_information.text = ""
            }

        }

        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.notifyDataSetChanged()

        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallBack(object: FavoriteAdapter.OnItemClickCallBack{
            override fun onItemClicked(userParcelable: UserParcelable) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, userParcelable)
                startActivity(intent)
            }
        })
    }
    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager= activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo= connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}

