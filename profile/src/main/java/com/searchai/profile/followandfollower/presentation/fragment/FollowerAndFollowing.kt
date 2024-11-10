package com.searchai.profile.followandfollower.presentation.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.searchai.common.kotlinExtentions.BaseBottomSheet
import com.searchai.common.kotlinExtentions.constants.BottomSheetConst
import com.searchai.profile.R
import com.searchai.profile.databinding.FragmentFollowerFollowingBinding
import com.searchai.profile.followandfollower.adapter.PagerAdapter

class FollowerAndFollowing:BaseBottomSheet<FragmentFollowerFollowingBinding>(
    R.layout.fragment_follower_following,
    FragmentFollowerFollowingBinding::bind
) {
    var count = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpFullScreen(bottomSheetDialog,BottomSheetConst.settingBottomSheetHeight)
        }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_SETTLING) {
                    count++
                    if(count % 2 == 0){
                        dialog.dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

        })
        dialog.behavior.isDraggable = false
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val pagerAdapter = PagerAdapter(requireActivity())
        binding.vpFollowerFollowing.adapter = pagerAdapter

        TabLayoutMediator(binding.tabFollowFollowing,binding.vpFollowerFollowing){tab,poistion->
            tab.text = when(poistion){
                0 -> "Followers"
                1 -> "Followings"
                else -> {null}
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
//        binding.vpFollowerFollowing.adapter =
//            FollowerFollowingAdapter(childFragmentManager, lifecycle)
//
//        TabLayoutMediator(
//            binding.tabFollowFollowing, binding.vpFollowerFollowing
//        ) { tab: TabLayout.Tab, position: Int ->
//
//            if (position == 0) {
//                tab.text = "Followers"
//            } else {
//                tab.text = "Following"
//            }
//
//        }.attach()
    }


}
