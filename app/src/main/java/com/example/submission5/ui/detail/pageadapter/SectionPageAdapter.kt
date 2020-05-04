package com.example.submission5.ui.detail.pageadapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.submission5.R
import com.example.submission5.ui.detail.fragment.FollowersFragment
import com.example.submission5.ui.detail.fragment.FollowingFragment

class SectionPagerAdapter(private val context: Context, fm : FragmentManager, mBundle: Bundle): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.title_followers, R.string.title_following)
    private val mBundle = mBundle

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position){
            0 -> {
                fragment = FollowersFragment()
                fragment.arguments = mBundle
            }
            1 -> {
                fragment = FollowingFragment()
                fragment.arguments = mBundle
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

}