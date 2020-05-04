package com.example.submission5.mainmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission5.BuildConfig.GITHUB_TOKEN
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class SearchUser : ViewModel() {
    var listUser = MutableLiveData<ArrayList<UserParcelable>>()
    fun setSearchUser(query : String){
        val token = GITHUB_TOKEN
        val url = "https://api.github.com/search/users?q=$query"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authentication", "token <$token>")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val listItems = ArrayList<UserParcelable>()
                    val result = String(responseBody!!)
                    val responseObjects = JSONObject(result)
                    val list = responseObjects.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val listApi = list.getJSONObject(i)
                        val listApiItems = UserParcelable()
                        listApiItems.username = listApi.getString("login")
                        listApiItems.image = listApi.getString("avatar_url")
                        listItems.add(listApiItems)
                    }
                    listUser.postValue(listItems)
                }catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure EK", error?.message.toString())
            }

        })
    }
    fun getSearchUser(): LiveData<ArrayList<UserParcelable>> {
        return listUser
    }
}