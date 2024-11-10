package com.searchai.profile.followandfollower.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.searchai.profile.followandfollower.presentation.tablayout.FollowerTab
import com.searchai.profile.followandfollower.presentation.tablayout.FollowingTab

class PagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

     private val fragmentList = listOf(
        FollowerTab(),FollowingTab()
     )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}