package com.example.submission5.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission5.R
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.example.submission5.room.UserDatabase
import com.example.submission5.ui.detail.DetailActivity
import com.example.submission5.widget.ImageBannerWidget
import com.example.submission5.widget.ImageBannerWidget.Companion.UPDATE_WIDGET
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.recycleview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private var favoriteAdapter: FavoriteAdapter? = null
    private var userDatabase: UserDatabase? = null
    private val listItems = ArrayList<UserParcelable>()
    override fun onRestart() {
        super.onRestart()
        listItems.clear()
        favoriteAdapter = FavoriteAdapter()
        val resId = R.anim.layout_animation_slide_from_bottom
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        animation.delay = 0.20f
        animation.order = LayoutAnimationController.ORDER_NORMAL

        recycleview.layoutAnimation = animation

        favoriteAdapter?.setData(listItems)
        recycleview.adapter?.notifyDataSetChanged()
        GlobalScope.launch {
            this.run {
                val user = userDatabase!!.userDao().getAll()
                if (user.size > 0) {
                    for (i in 0 until user.size) {
                        val getItems = UserParcelable()
                        getItems.username = user[i].username
                        getItems.image = user[i].image
                        getItems.name = user[i].name
                        getItems.company = user[i].company
                        getItems.blog = user[i].blog
                        getItems.location = user[i].location
                        getItems.repositori = user[i].repositori
                        listItems.add(getItems)
                    }
                    tv_information.text = ""
                    progressBar.visibility = View.INVISIBLE
                    favoriteAdapter?.setData(listItems)
                }
                else{
                    progressBar.visibility = View.INVISIBLE
                    tv_information.text = getString(R.string.found)
                }
            }
        }
        favoriteAdapter?.notifyDataSetChanged()
        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = favoriteAdapter

        favoriteAdapter?.setOnItemClickCallBack(object: FavoriteAdapter.OnItemClickCallBack{
            override fun onItemClicked(userParcelable: UserParcelable) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, userParcelable)
                startActivity(intent)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteAdapter = FavoriteAdapter()
        userDatabase = UserDatabase.getInstance(this)
        progressBar.visibility = View.VISIBLE

        val resId = R.anim.layout_animation_slide_from_bottom
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        animation.delay = 0.20f
        animation.order = LayoutAnimationController.ORDER_NORMAL

        recycleview.layoutAnimation = animation

        GlobalScope.launch {
            coroutineContext.let {
                val user = userDatabase!!.userDao().getAll()
                if (user.size > 0) {
                    for (i in 0 until user.size) {
                        val getItems = UserParcelable()
                        getItems.username = user[i].username
                        getItems.image = user[i].image
                        getItems.name = user[i].name
                        getItems.company = user[i].company
                        getItems.blog = user[i].blog
                        getItems.location = user[i].location
                        getItems.repositori = user[i].repositori
                        listItems.add(getItems)
                    }
                    tv_information.text = ""
                    progressBar.visibility = View.INVISIBLE
                    favoriteAdapter?.setData(listItems)
                }
                else{
                    progressBar.visibility = View.INVISIBLE
                    tv_information.text = getString(R.string.found)
                }
            }
        }
        supportActionBar?.setTitle("User Favorite")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteAdapter?.notifyDataSetChanged()

        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = favoriteAdapter

        favoriteAdapter?.setOnItemClickCallBack(object: FavoriteAdapter.OnItemClickCallBack{
            override fun onItemClicked(userParcelable: UserParcelable) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, userParcelable)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_del_all, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.menu_del_all -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure to delete all favorite user?")
                builder.setPositiveButton("Yes"){
                    _, _ ->
                    GlobalScope.launch {
                        this.let {
                            userDatabase!!.userDao().delete_all()
                        }
                    }
                    Toast.makeText(applicationContext, "Delete Success", Toast.LENGTH_SHORT).show()
                    listItems.clear()
                    favoriteAdapter?.setData(listItems)
                    favoriteAdapter?.notifyDataSetChanged()
                    val intent = Intent(this, ImageBannerWidget::class.java)
                    intent.setAction(UPDATE_WIDGET)
                    this.sendBroadcast(intent)

                }
                builder.setNegativeButton("No"){
                    _, _ -> Toast.makeText(applicationContext, "Delete cancel", Toast.LENGTH_SHORT).show()
                }
                builder.show()
                onRestart()
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed()
        }
        else {
            supportFragmentManager.popBackStack()
        }
    }
}
