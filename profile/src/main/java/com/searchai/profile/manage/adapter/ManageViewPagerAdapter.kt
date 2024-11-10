package com.searchai.profile.manage.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.searchai.profile.manage.presentation.tablayouts.live.presentation.fragment.Live
import com.searchai.profile.manage.presentation.tablayouts.request.presentation.fragment.Request
import com.searchai.profile.manage.presentation.tablayouts.schedule.presentation.fragment.Schedule

class ManageViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        Request(), Live(), Schedule()
    )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}