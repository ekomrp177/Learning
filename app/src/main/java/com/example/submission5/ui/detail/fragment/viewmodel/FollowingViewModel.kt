package com.example.submission5.ui.detail.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission5.BuildConfig.GITHUB_TOKEN
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserParcelable>>()
    fun setFollowing(query : String){
        val listItems = ArrayList<UserParcelable>()
        val token = GITHUB_TOKEN
        val url = "https://api.github.com/users/$query/following"
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
                    val responseObjects = JSONArray(result)
                    val list = responseObjects
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
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.d("onFailure", throwable?.message.toString())
            }

        })
    }
    fun getFollowing(): LiveData<ArrayList<UserParcelable>> {
        return listUser
    }
}