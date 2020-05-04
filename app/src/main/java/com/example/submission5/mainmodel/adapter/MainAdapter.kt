package com.example.submission5.mainmodel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.submission5.R
import com.example.submission5.mainmodel.parcelable.UserParcelable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var onItemClickCallBack : OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack{
        fun onItemClicked(userParcelable: UserParcelable)
    }

    private val mData = ArrayList<UserParcelable>()
    fun setData(items: ArrayList<UserParcelable>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return MainViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class MainViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        fun bind(userParcelable: UserParcelable){
            with (itemView){
                user_name.text = userParcelable.username
                Picasso.get().load(userParcelable.image).into(user_image)
            }
            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(userParcelable) }
        }
    }
}