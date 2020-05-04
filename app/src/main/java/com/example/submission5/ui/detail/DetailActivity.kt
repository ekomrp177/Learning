package com.example.submission5.ui.detail

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.submission5.R
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.example.submission5.room.UserDatabase
import com.example.submission5.ui.detail.fragment.FollowersFragment
import com.example.submission5.ui.detail.fragment.FollowingFragment
import com.example.submission5.ui.detail.pageadapter.SectionPagerAdapter
import com.example.submission5.widget.ImageBannerWidget
import com.example.submission5.widget.ImageBannerWidget.Companion.UPDATE_WIDGET
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {
    companion object{
        var EXTRA_DATA = "save"
    }
    private var userDatabase: UserDatabase? = null
    private var isFav : Boolean = false
    private var strName : String? = null
    private var strWebUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        userDatabase = UserDatabase.getInstance(this)

        var progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.isIndeterminate
        progressDialog.setCancelable(false)
        progressDialog.show()

        val user = intent.getParcelableExtra<UserParcelable>(EXTRA_DATA)
        user_username.text = "ID : "+user?.username
        Picasso.get().load(user?.image).into(user_image)
        strName = user?.username
        strWebUrl = user?.blog
        user_company.text = "Company : "+user?.company
        user_name_detail.text = user?.name
        user_location.text =  user?.location
        user_repositori.text = "Repositori : "+user?.repositori

        Handler().postDelayed({
            progressDialog.dismiss()
        }, 2000)
        GlobalScope.launch {
            this.let {
                val userCheck = userDatabase!!.userDao().getAll()
                if (userCheck.size > 0) {
                    for (i in 0 until userCheck.size) {
                        if(userCheck[i].username == user?.username){
                            isFav = true
                            fab_favorite.setImageResource(R.drawable.ic_already_fav)
                            break
                        }
                    }
                }
                else {
                    isFav = false
                    fab_favorite.setImageResource(R.drawable.ic_add_fav)
                }
            }
        }

        val mBundle = Bundle()
        mBundle.putString(FollowingFragment.GET_USERNAME1, user?.username)
        mBundle.putString(FollowersFragment.GET_USERNAME, user?.username)
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, mBundle)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f


        fab_favorite.setOnClickListener {view ->
            if (isFav){
                fab_favorite.setImageResource(R.drawable.ic_add_fav)
                Snackbar.make(view, "Removed from favorite", Snackbar.LENGTH_SHORT).show()
                GlobalScope.launch {
                    this.let {
                        userDatabase!!.userDao().delete(user?.username)
                    }
                }
                val intent = Intent(this, ImageBannerWidget::class.java)
                intent.setAction(UPDATE_WIDGET)
                this.sendBroadcast(intent)
                isFav = false
            }
            else{
                if (user?.username != null) {
                    fab_favorite.setImageResource(R.drawable.ic_already_fav)
                    Snackbar.make(view, "Added to favorite", Snackbar.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        this.let {
                            userDatabase!!.userDao().insert(user)
                        }
                    }
                    val intent = Intent(this, ImageBannerWidget::class.java)
                    intent.setAction(UPDATE_WIDGET)
                    this.sendBroadcast(intent)
                    isFav = true
                }else{
                    Snackbar.make(view, "Cannot add to favorite", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_info -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Website ${strName}")
                builder.setMessage("Dou you want to go to this website?\n${strWebUrl}")
                builder.setPositiveButton("Yes"){
                        _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strWebUrl))
                    startActivity(intent)
                }
                builder.setNegativeButton("No"){
                        _, _ ->
                }
                builder.show()
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed()
            finish()
        }
        else {
            supportFragmentManager.popBackStack()
            finish()
        }
    }
}