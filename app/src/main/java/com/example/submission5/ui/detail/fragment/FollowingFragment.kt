package com.example.submission5.ui.detail.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.submission5.R
import com.example.submission5.mainmodel.adapter.MainAdapter
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.example.submission5.ui.detail.DetailUserActivity
import com.example.submission5.ui.detail.fragment.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.recycleview
import kotlinx.android.synthetic.main.fragment_following.tv_foundata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment : Fragment() {
    companion object{
        var GET_USERNAME1 = "username"
    }
    private lateinit var mainAdapter : MainAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainAdapter = MainAdapter()
        mainAdapter.notifyDataSetChanged()

        recycleview.layoutManager = LinearLayoutManager(context)
        recycleview.adapter = mainAdapter
        tv_foundata.text = "Loading..."
        GlobalScope.launch(context = Dispatchers.Main){
                delay(2000)
                if (!verifyAvailableNetwork() && mainAdapter.itemCount == 0){
                    tv_foundata.text = "Check your internet connection!"
                }
                else if(mainAdapter.itemCount == 0) {
                    tv_foundata.text = "Access is limited!\nPlease try again in few minutes..."
                }

        }

        val idQuery = arguments?.getString(FollowingFragment.GET_USERNAME1)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)

        followingViewModel.setFollowing(idQuery!!)
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
                UserParcelable ->
            if (UserParcelable != null){
                mainAdapter.setData(UserParcelable)
                if(UserParcelable.size == 0){
                    tv_foundata.text = "Don't have following"
                }
                else{
                    tv_foundata.text = ""
                }
            }
        })

        mainAdapter.setOnItemClickCallBack(object : MainAdapter.OnItemClickCallBack{
            override fun onItemClicked(userParcelable: UserParcelable) {
                var intent = Intent(context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_DATA_PLUS, userParcelable)
                startActivity(intent)
            }
        })
    }
    fun verifyAvailableNetwork():Boolean{
        val connectivityManager= activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}
