package com.example.myfavoriteuser.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.myfavoriteuser.R
import com.example.myfavoriteuser.room.UserParcelable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object{
        var EXTRA_DATA = "save"
    }
    private var strName : String? = null
    private var strWebUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.isIndeterminate
        progressDialog.setCancelable(false)
        progressDialog.show()

        val user = intent.getParcelableExtra<UserParcelable>(EXTRA_DATA)
        user_username.text = "ID : "+user.username
        Picasso.get().load(user.image).into(user_image)
        strName = user.username
        strWebUrl = user.blog
        user_company.text = "Company : "+user.company
        user_name_detail.text = user.name
        user_location.text =  user.location
        user_repositori.text = "Repositori : "+user.repositori


        Handler().postDelayed({
            progressDialog.dismiss()
        }, 500)

        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
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
