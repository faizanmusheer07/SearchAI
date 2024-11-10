package com.searchai.profile.tablayouts.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.searchai.profile.tablayouts.more.More
import com.searchai.profile.tablayouts.room.presentation.fragment.Room
import com.searchai.profile.tablayouts.video.presentation.fragment.Video

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        Room(),
        Video(),
        More()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
