package com.example.submission5.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission5.BuildConfig.GITHUB_TOKEN
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel : ViewModel() {
    var listUser = MutableLiveData<UserParcelable>()
    fun setUser(query : String?){
        val token = GITHUB_TOKEN
        val url = "https://api.github.com/users/$query"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authentication", "token <$token>")
        client.get(url, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                try {
                    val result = (responseString!!)
                    val responseObjects = JSONObject(result)
                    val listApi = responseObjects
                    val listApiItems = UserParcelable()
                    listApiItems.name = listApi.getString("name")
                    listApiItems.username = listApi.getString("login")
                    listApiItems.image = listApi.getString("avatar_url")
                    listApiItems.company = listApi.getString("company")
                    listApiItems.location = listApi.getString("location")
                    listApiItems.blog = listApi.getString("blog")
                    listApiItems.repositori = listApi.getString("public_repos")

                    if(listApiItems.name == "null") listApiItems.name = "-"
                    if(listApiItems.blog == "null") listApiItems.blog = "-"
                    if(listApiItems.company == "null") listApiItems.company = "-"
                    if(listApiItems.location == "null") listApiItems.location = "-"
                    if(listApiItems.repositori == "null") listApiItems.repositori = "-"
                    listUser.postValue(listApiItems)
                }catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                error: Throwable?
            ) {
                Log.d("onFailure EK", error?.message.toString())
            }

        })
    }
    fun getUser(): LiveData<UserParcelable> {
        return listUser
    }
}